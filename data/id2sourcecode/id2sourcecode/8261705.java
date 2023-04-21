    public Channel getChannel(byte channelId) {
        if (!isChannelUsed(channelId)) {
            channels[channelId] = new Channel(this, channelId);
        }
        return channels[channelId];
    }
