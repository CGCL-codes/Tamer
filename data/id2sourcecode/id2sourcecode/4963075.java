    public static <CT> JobDataFlow<CT> make(DataFlowNetwork<CT> network, ContextJobAdapter<CT> adapter, Job EOF_JOB) {
        JobDataFlow<CT> result = new JobDataFlow<CT>();
        result.setJobAdapter(adapter);
        for (Channel<?> channel : network.getChannels()) result.addChannel(new Channel<Object>(channel.getId(), channel.getEOS(), channel.getCapacity()));
        result.addChannel(new Channel<Throwable>(EXCEPTION_CHANNEL_ID, EOF_EXCEPTION));
        result.addChannel(new Channel<Job>(JOB_CHANNEL_ID, EOF_JOB));
        for (Processor<CT> node : network.getNodes()) {
            Set<String> ins = new HashSet<String>();
            Set<String> outs = new HashSet<String>();
            ins.add(JOB_CHANNEL_ID);
            outs.add(EXCEPTION_CHANNEL_ID);
            ins.addAll(network.getInputs(node));
            outs.addAll(network.getOutputs(node));
            result.addNode(new JobProcessor<CT>(result, node), ins, outs);
        }
        result.setContext(network.getContext());
        return result;
    }
