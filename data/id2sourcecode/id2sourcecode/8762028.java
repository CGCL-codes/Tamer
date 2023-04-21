    public ChannelDisplayProperties getChannelDisplayProperties(int channel) {
        checkChannel(channel);
        return chDisplayProps[channel - 1];
    }
