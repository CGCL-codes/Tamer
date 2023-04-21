    @SuppressWarnings("unchecked")
    public void process(ContextType ctx, Ports ports) throws InterruptedException {
        InputPort<Job> jobIn = ports.getInput(network.getJobChannel().getId());
        while (disconnectSignal == null) {
            synchronized (pauseLock) {
                if (pauseRequested) {
                    paused = true;
                    synchronized (pauseRequestLock) {
                        pauseRequested = false;
                        pauseRequestLock.notifyAll();
                    }
                    while (paused) pauseLock.wait();
                }
            }
            Job job = null;
            if (pendingJobs != null && !pendingJobs.isEmpty()) job = pendingJobs.removeFirst(); else {
                job = jobIn.poll();
                if (job == null) try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException ex) {
                    break;
                }
            }
            if (jobIn.isEOS(job)) break;
            ContextType CC = ctx;
            if (network.getJobAdapter() != null) {
                try {
                    CC = network.getJobAdapter().adapt(ctx, job);
                } catch (Throwable t) {
                    continue;
                }
            }
            currentJob = job;
            for (JobListener<ContextType> l : listeners.toArray(new JobListener[0])) l.startJob(currentJob, CC, this);
            Ports innerPorts = new Ports();
            for (InputPort<?> p : ports.getInputPorts()) if (!JobDataFlow.isChannelJobSpecific(p.getChannel().getId())) continue; else {
                JobChannel<?> ch = network.getChannelManager().getJobChannel(network, p.getChannel(), job);
                InputPort<?> port = ch.newInputPort();
                if (monitoringOn) port = new MonitoringInputPort(port, getAvgAccumulator(p.getChannel().getId()));
                if (processor instanceof LoadBalancedNode || processor instanceof InputSplitter || processor instanceof OutputCombiner) innerPorts.addPort(port); else innerPorts.getInputMap().put(LoadBalancer.originalChannelId(port.getChannel().getId()), port);
            }
            for (OutputPort<?> p : ports.getOutputPorts()) if (!JobDataFlow.isChannelJobSpecific(p.getChannel().getId())) continue; else {
                JobChannel<?> ch = network.getChannelManager().getJobChannel(network, p.getChannel(), job);
                OutputPort<?> port = ch.newOutputPort();
                if (monitoringOn) port = new MonitoringOutputPort(port, getAvgAccumulator(p.getChannel().getId()));
                if (processor instanceof LoadBalancedNode || processor instanceof InputSplitter || processor instanceof OutputCombiner) innerPorts.addPort(port); else innerPorts.getOutputMap().put(LoadBalancer.originalChannelId(port.getChannel().getId()), port);
            }
            DistributedException ex = null;
            try {
                innerPorts.openAll();
                processor.process(CC, innerPorts);
            } catch (Throwable t) {
                System.err.println("Processor " + getName() + " bailed out, stack trace follows...");
                t.printStackTrace();
                ex = new DistributedException(this, currentJob, t.getMessage(), t);
                network.getExceptionChannel().put(ex);
            } finally {
                innerPorts.closeAll();
                ArrayList<DistributedException> exL = new ArrayList<DistributedException>();
                if (ex != null) exL.add(ex);
                for (JobListener<ContextType> l : listeners.toArray(new JobListener[0])) l.endJob(currentJob, CC, this, exL);
                currentJob = null;
            }
        }
        if (disconnectSignal != null) {
            pendingJobs = new LinkedList<Job>();
            for (Job pending = jobIn.poll(); pending != null && !jobIn.isEOS(pending); pending = jobIn.poll()) pendingJobs.add(pending);
            synchronized (disconnectSignal) {
                disconnectSignal.notifyAll();
            }
        }
    }
