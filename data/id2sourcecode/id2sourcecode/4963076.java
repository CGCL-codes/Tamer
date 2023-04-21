    public static <CT> JobDataFlow<CT> clone(JobDataFlow<CT> network) {
        try {
            JobDataFlow<CT> result = new JobDataFlow<CT>();
            result.setJobAdapter(network.getJobAdapter());
            for (Channel<?> channel : network.getChannels()) result.addChannel(new Channel<Object>(channel.getId(), channel.getEOS(), channel.getCapacity()));
            Mapping<Pair<Object, String>, Boolean> propertyFilter = new Mapping<Pair<Object, String>, Boolean>() {

                public Boolean eval(Pair<Object, String> p) {
                    return !(p.getFirst() instanceof Processor) || !p.getSecond().equals("network");
                }
            };
            for (Processor<CT> node : network.getNodes()) result.addNode((Processor<CT>) HGUtils.cloneObject(node, propertyFilter), network.getInputs(node), network.getOutputs(node));
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
