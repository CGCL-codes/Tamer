    public void setBandwidthConfigure(IBandwidthConfigure config) {
        IFlowControlService fcs = (IFlowControlService) getScope().getContext().getBean(IFlowControlService.KEY);
        this.bandwidthConfig = config;
        fcs.updateBWConfigure(this);
        if (config.getDownstreamBandwidth() > 0) {
            ServerBW serverBW = new ServerBW((int) config.getDownstreamBandwidth() / 8);
            getChannel((byte) 2).write(serverBW);
        }
        if (config.getUpstreamBandwidth() > 0) {
            ClientBW clientBW = new ClientBW((int) config.getUpstreamBandwidth() / 8, (byte) 0);
            getChannel((byte) 2).write(clientBW);
            bytesReadInterval = (int) config.getUpstreamBandwidth() / 8;
            nextBytesRead = (int) getWrittenBytes();
        }
    }
