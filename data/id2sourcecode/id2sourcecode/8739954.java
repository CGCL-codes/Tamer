    @SuppressWarnings("unchecked")
    private static <ContextType> Processor<ContextType> cloneProcessor(DataFlowNetwork<ContextType> network, Processor<ContextType> processor, Set<String> inputs, Set<String> outputs, Integer channelsId, Integer processorId, Set<String> splitterOutputs, Set<String> combinerInputs) throws Exception {
        Mapping<Pair<Object, String>, Boolean> propertyFilter = new Mapping<Pair<Object, String>, Boolean>() {

            public Boolean eval(Pair<Object, String> p) {
                return !(p.getFirst() instanceof Processor) || !p.getSecond().equals("network");
            }
        };
        boolean isJobProcessor = processor instanceof JobProcessor;
        Processor<ContextType> copy = (Processor<ContextType>) HGUtils.cloneObject(processor, propertyFilter);
        Set<String> copyInputs = new HashSet<String>();
        for (String inId : inputs) {
            if (isJobProcessor && !JobDataFlow.isChannelJobSpecific(inId)) {
                copyInputs.add(inId);
                combinerInputs.add(inId);
                continue;
            }
            String copyId = makeChannelId(inId, makeInputChannelPrefix(channelsId), processorId);
            copyInputs.add(copyId);
            Channel<?> ch = network.getChannel(inId);
            network.addChannel(new Channel(copyId, ch.getEOS(), ch.getCapacity()));
            splitterOutputs.add(copyId);
        }
        Set<String> copyOutputs = new HashSet<String>();
        for (String outId : outputs) {
            if (isJobProcessor && !JobDataFlow.isChannelJobSpecific(outId)) {
                copyOutputs.add(outId);
                splitterOutputs.add(outId);
                continue;
            }
            String copyId = makeChannelId(outId, makeOutputChannelPrefix(channelsId), processorId);
            copyOutputs.add(copyId);
            Channel<?> ch = network.getChannel(outId);
            network.addChannel(new Channel(copyId, ch.getEOS(), ch.getCapacity()));
            combinerInputs.add(copyId);
        }
        if (isJobProcessor) {
            JobProcessor jp = (JobProcessor) copy;
            jp.setProcessor(new LoadBalancedNode(jp.getProcessor()));
        } else copy = new LoadBalancedNode(copy);
        network.addNode(copy, copyInputs, copyOutputs);
        return copy;
    }
