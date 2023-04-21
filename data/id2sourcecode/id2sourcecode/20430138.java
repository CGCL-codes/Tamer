    public int getIndex() {
        ChannelList list = this.getContext();
        return list == null ? -1 : list.getChannels().indexOf(this);
    }
