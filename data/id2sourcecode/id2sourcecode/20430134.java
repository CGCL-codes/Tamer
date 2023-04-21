    public ChannelIdentification getIdentification() {
        ChannelList list = this.getContext();
        PVRState state;
        if (list == null) return null;
        state = list.getContext();
        if (state == null) return null;
        return state.getNetworkMap().getChannelIdentification(this);
    }
