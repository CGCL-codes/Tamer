    public ChannelImagePlus getChannelImagePlus(int channel) {
        checkChannel(channel);
        return channelImps[channel - 1];
    }
