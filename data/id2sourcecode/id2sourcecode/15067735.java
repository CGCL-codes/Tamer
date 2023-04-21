    public Integer getChannelIndex(Channel channel) {
        for (int i = 0; i < this.openChannels.length; i++) if (channel == Channel.Guild && this.openChannels[i] > 1000000000) return i; else if (this.openChannels[i] == channel.getId()) return i;
        return null;
    }
