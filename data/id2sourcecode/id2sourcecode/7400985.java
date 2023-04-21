    @SuppressWarnings("unchecked")
    private void loadJobNetwork() {
        HyperGraph graph = hgpeer.getGraph();
        network = graph.get(networkHandle);
        if (network == null) return;
        network.clearAll();
        List<HGLink> processors = hg.getAll(graph, hg.orderedLink(networkHandle, hg.anyHandle(), hg.anyHandle()));
        channelPeer = new ChannelPeer<ContextType>(hgpeer, network);
        network.setChannelManager(channelPeer);
        for (HGLink l : processors) {
            HGHandle peerId = l.getTargetAt(2);
            if (!peerId.equals(hgpeer.getIdentity().getId())) continue;
            List<ChannelLink<Job>> channels = hg.getAll(graph, hg.and(hg.type(ChannelLink.class), hg.incident(l.getTargetAt(1))));
            for (ChannelLink<Job> ch : channels) network.addChannel(ch.getChannel());
        }
        for (HGLink l : processors) {
            HGHandle peerId = l.getTargetAt(2);
            HGHandle processorHandle = l.getTargetAt(1);
            JobProcessor<ContextType> processor = graph.get(processorHandle);
            List<ChannelLink<Job>> channels = hg.getAll(graph, hg.and(hg.type(ChannelLink.class), hg.incident(l.getTargetAt(1))));
            if (peerId.equals(hgpeer.getIdentity().getId())) {
                ArrayList<String> inputs = new ArrayList<String>();
                ArrayList<String> outputs = new ArrayList<String>();
                for (ChannelLink<Job> ch : channels) {
                    if (ch.getHead().contains(processorHandle)) inputs.add(ch.getChannel().getId()); else outputs.add(ch.getChannel().getId());
                }
                processor.setNetwork(network);
                network.addNode(processor, inputs.toArray(new String[0]), outputs.toArray(new String[0]));
            } else {
                for (ChannelLink<Job> ch : channels) if (network.getChannel(ch.getChannel().getId()) != null) channelPeer.addChannelPeer(ch.getChannel().getId(), (HGPeerIdentity) graph.get(peerId));
            }
        }
        if (network.getChannel(JobDataFlow.JOB_CHANNEL_ID) != null) network.addNode(new JobTrackProcessor(), new String[] { JobDataFlow.JOB_CHANNEL_ID }, new String[] {});
        channelPeer.initNetworking();
    }
