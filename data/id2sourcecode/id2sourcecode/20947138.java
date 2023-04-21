    private ETLThreadManager compileJob(Element job) throws ParserConfigurationException, SQLException, Exception {
        NodeList ls = job.getElementsByTagName("STEP");
        int batchSize = XMLHelper.getAttributeAsInt(job.getAttributes(), "BATCHSIZE", 1000);
        int queueSize = XMLHelper.getAttributeAsInt(job.getAttributes(), "QUEUESIZE", 5);
        HashMap writers = new HashMap();
        HashMap readers = new HashMap();
        HashMap transforms = new HashMap();
        HashMap splitters = new HashMap();
        HashMap mergers = new HashMap();
        ArrayList pendingInstantiation = new ArrayList();
        for (int i = 0; i < ls.getLength(); i++) {
            Node node = ls.item(i);
            if (node.hasAttributes() == false) continue;
            NamedNodeMap nmAttrs = node.getAttributes();
            String className = XMLHelper.getAttributeAsString(nmAttrs, "CLASS", null);
            ETLWorker.setOutDefaults((Element) node);
            if (className == null) throw new KETLThreadException("Step has no class attribute, check XML", this);
            Class cl = Class.forName(className);
            boolean disableStep = XMLHelper.getAttributeAsBoolean(nmAttrs, "DISABLESTEP", false);
            if (disableStep && com.kni.etl.ketl.writer.ETLWriter.class.isAssignableFrom(cl)) {
                cl = com.kni.etl.ketl.writer.NullWriter.class;
            }
            String name = XMLHelper.getAttributeAsString(nmAttrs, "NAME", null);
            if (name == null) throw new KETLThreadException("Step has no name, check XML", this);
            Step step = new Step((Element) node, cl, name);
            if (ETLWriter.class.isAssignableFrom(cl)) writers.put(name, step); else if (ETLReader.class.isAssignableFrom(cl)) readers.put(name, step); else if (ETLTransform.class.isAssignableFrom(cl)) transforms.put(name, step); else if (ETLSplit.class.isAssignableFrom(cl)) splitters.put(name, step); else if (ETLMerge.class.isAssignableFrom(cl)) mergers.put(name, step);
            if (((Element) step.getConfig()).hasAttribute("BATCHSIZE") == false) ((Element) step.getConfig()).setAttribute("BATCHSIZE", Integer.toString(batchSize));
            if (((Element) step.getConfig()).hasAttribute("QUEUESIZE") == false) ((Element) step.getConfig()).setAttribute("QUEUESIZE", Integer.toString(queueSize));
            pendingInstantiation.add(step);
        }
        this.em = new ETLThreadManager(this);
        int partitions = XMLHelper.getAttributeAsInt(job.getAttributes(), "PARRALLISM", 1);
        HashMap readySources = new HashMap();
        for (Object o : readers.entrySet()) {
            Map.Entry node = ((Map.Entry) o);
            Step step = (Step) node.getValue();
            int instancePartitions = XMLHelper.getAttributeAsInt(step.getConfig().getAttributes(), "PARRALLISM", partitions);
            if (instancePartitions != 1 && instancePartitions != partitions) throw new KETLThreadException("Reader parrallism must either be 1 or equal to the job parallism of " + partitions, this);
            step.setThreadGroup(ETLThreadGroup.newInstance(null, ETLThreadManager.getThreadingType((Element) step.getConfig()), step, instancePartitions, this.em));
            readySources.put(step.getName(), step);
            pendingInstantiation.remove(step);
        }
        while (pendingInstantiation.size() > 0) {
            int pendingSize = pendingInstantiation.size();
            for (Object o : pendingInstantiation) {
                Step currentStep = (Step) o;
                String[] sourceNames = ETLWorker.getSource((Element) currentStep.getConfig());
                if (mergers.containsKey(currentStep.getName())) {
                    if (sourceNames.length != 2 || sourceNames[ETLWorker.LEFT] == null || sourceNames[ETLWorker.RIGHT] == null) throw new KETLThreadException("LEFT and RIGHT source need to be specified", this);
                    if (readySources.containsKey(sourceNames[ETLWorker.LEFT]) && readySources.containsKey(sourceNames[ETLWorker.RIGHT])) {
                        currentStep.setThreadGroup(ETLThreadGroup.newInstance(((Step) readySources.get(sourceNames[ETLWorker.LEFT])).getThreadGroup(ETLWorker.getChannel((Element) currentStep.getConfig(), ETLWorker.LEFT)), ((Step) readySources.get(sourceNames[ETLWorker.RIGHT])).getThreadGroup(ETLWorker.getChannel((Element) currentStep.getConfig(), ETLWorker.RIGHT)), ETLThreadGroup.PIPELINE_MERGE, currentStep, partitions, this.em));
                        readySources.put(currentStep.getName(), currentStep);
                    }
                } else {
                    if (sourceNames.length != 1) throw new KETLThreadException("Step " + currentStep.getName() + " does not support multiple sources", this);
                    Step sourceStep = (Step) readySources.get(sourceNames[ETLWorker.DEFAULT]);
                    if (sourceStep == null) continue;
                    if (splitters.containsKey(currentStep.getName())) {
                        currentStep.setThreadGroups(ETLThreadGroup.newInstances(sourceStep.getThreadGroup(ETLWorker.getChannel((Element) currentStep.getConfig(), ETLWorker.DEFAULT)), ETLWorker.getChannels((Element) currentStep.getConfig()), ETLThreadGroup.PIPELINE_SPLIT, currentStep, partitions, this.em));
                        readySources.put(currentStep.getName(), currentStep);
                    } else {
                        currentStep.setThreadGroup(ETLThreadGroup.newInstance(sourceStep.getThreadGroup(ETLWorker.getChannel((Element) currentStep.getConfig(), ETLWorker.DEFAULT)), ETLThreadManager.getThreadingType((Element) currentStep.getConfig()), currentStep, partitions, this.em));
                        readySources.put(currentStep.getName(), currentStep);
                    }
                }
            }
            pendingInstantiation.removeAll(readySources.values());
            if (pendingSize == pendingInstantiation.size()) throw new KETLThreadException("Step channel mapping error, check xml for unknown sources, check the following steps for source reference errors " + Arrays.toString(pendingInstantiation.toArray()), this);
        }
        KETLJobExecutor.checkForNonAssignedChannels(splitters.values().toArray());
        KETLJobExecutor.checkForNonAssignedChannels(readers.values().toArray());
        KETLJobExecutor.checkForNonAssignedChannels(transforms.values().toArray());
        return this.em;
    }
