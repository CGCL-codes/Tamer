    public ChannelInformation getChannel() {
        PVRState state = this.getContext();
        if (state == null) return null;
        return state.getChannel(this.getServiceType(), this.getServiceId());
    }
