    public Integer getOpenChannelIndex(Channel channel) {
        if (this.channels != null) return this.channels.getChannelIndex(channel);
        return null;
    }
