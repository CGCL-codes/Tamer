    public static <T> HGHandle saveDataFlowNetwork(final HyperGraph graph, final DataFlowNetwork<T> network, final HGHandle peerIdentity) {
        return graph.getTransactionManager().ensureTransaction(new Callable<HGHandle>() {

            public HGHandle call() {
                HGHandle networkHandle = graph.getHandle(network);
                if (networkHandle == null) networkHandle = graph.add(network); else throw new RuntimeException("Network with handle " + networkHandle + " already saved.");
                Map<Object, Set<HGHandle>> channelInputs = new HashMap<Object, Set<HGHandle>>();
                Map<Object, Set<HGHandle>> channelOutputs = new HashMap<Object, Set<HGHandle>>();
                for (Channel<?> ch : network.getChannels()) {
                    channelInputs.put(ch.getId(), new HashSet<HGHandle>());
                    channelOutputs.put(ch.getId(), new HashSet<HGHandle>());
                }
                for (Processor<T> node : network.getNodes()) {
                    HGHandle h = graph.add(node);
                    if (peerIdentity == null) graph.add(new HGPlainLink(networkHandle, h)); else graph.add(new HGPlainLink(networkHandle, h, peerIdentity));
                    for (Object chId : network.getInputs(node)) channelInputs.get(chId).add(h);
                    for (Object chId : network.getOutputs(node)) channelOutputs.get(chId).add(h);
                }
                for (Channel<?> ch : network.getChannels()) graph.add(new ChannelLink<Object>((Channel<Object>) ch, channelInputs.get(ch.getId()), channelOutputs.get(ch.getId())));
                return networkHandle;
            }
        });
    }
