    public int getChannelId() {
        int channelIndex = -1;
        if (channel != null) {
            channelIndex = channel.getId();
        }
        return channelIndex;
    }
