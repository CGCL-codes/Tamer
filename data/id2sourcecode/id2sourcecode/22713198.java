    public ChannelIdentification getChannelIdentification(ChannelInformation info) {
        NetworkIdentification network = this.getNetwork(info.getOriginatingNetworkId());
        ChannelIdentification channel = network.getChannel(info.getLogicalChannel());
        if (channel != null) return channel;
        return network.createChannel(info.getLogicalChannel());
    }
