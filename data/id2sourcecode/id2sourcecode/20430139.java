    public Event getEvent(Date time) {
        ChannelList list = this.getContext();
        int index;
        PVRState state;
        if (list == null) return null;
        index = list.getChannels().indexOf(this);
        if (index < 0) return null;
        state = list.getContext();
        if (state == null) return null;
        return state.findEvent(list.getType(), index, time);
    }
