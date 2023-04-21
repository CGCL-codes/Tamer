    public static <T> DataFlowNetwork<T> loadNetwork(HyperGraph graph, HGHandle networkHandle) {
        DataFlowNetwork<T> result = graph.get(networkHandle);
        if (result == null) return null;
        result.clearAll();
        List<HGHandle> processors = hg.findAll(graph, hg.apply(hg.targetAt(graph, 1), hg.orderedLink(networkHandle, hg.anyHandle())));
        for (HGHandle h : processors) {
            List<ChannelLink<?>> L = hg.getAll(graph, hg.and(hg.type(ChannelLink.class), hg.incident(h)));
            for (ChannelLink<?> x : L) result.addChannel(x.getChannel());
        }
        for (HGHandle h : processors) {
            List<ChannelLink<?>> L = hg.getAll(graph, hg.and(hg.type(ChannelLink.class), hg.incident(h)));
            List<Object> inputs = new ArrayList<Object>();
            List<Object> outputs = new ArrayList<Object>();
            for (ChannelLink<?> x : L) {
                if (x.getHead().contains(h)) inputs.add(x.getChannel().getId()); else outputs.add(x.getChannel().getId());
            }
            Processor<T> processor = graph.get(h);
            if (processor instanceof JobProcessor) ((JobProcessor) processor).setNetwork((JobDataFlow) result);
            result.addNode(processor, inputs.toArray(new String[0]), outputs.toArray(new String[0]));
        }
        return result;
    }
