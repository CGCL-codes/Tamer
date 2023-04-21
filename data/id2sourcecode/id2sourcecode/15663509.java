    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initNetworking() {
        for (Channel<?> ch : network.getChannels()) {
            if (!JobDataFlow.isChannelJobSpecific(ch.getId())) {
                network.addChannel(new DistChannel(ch.getId(), ch.getEOS(), ch.getCapacity()));
            }
        }
        HGHandle netHandle = thisPeer.getGraph().getHandle(network);
        assert netHandle != null : new RuntimeException("Network " + network + " not in HyperGraph");
        this.activity = new NetworkPeerActivity(thisPeer, netHandle);
        thisPeer.getActivityManager().initiateActivity(activity);
    }
