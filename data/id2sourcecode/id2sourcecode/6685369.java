    public synchronized Channel getChannel() {
        if (channel == null) {
            Object channelId = getClientData("channelId");
            Channel channel = null;
            if (channelId != null) channel = getServer().getChannels().get(channelId.toString());
            this.channel = channel;
        }
        return channel;
    }
