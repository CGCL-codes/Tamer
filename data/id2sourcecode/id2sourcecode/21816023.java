    public ChannelInformation getChannel(int index) {
        return index >= this.channels.size() ? null : this.channels.get(index);
    }
