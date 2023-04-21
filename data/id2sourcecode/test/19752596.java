    public Channel getChannel(ChannelInfo channelInfo) {
        if (channelInfo == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < channels.length; i++) {
            if (channelInfo == channels[i]) {
                return channels[i];
            }
        }
        throw new IllegalArgumentException("This channel is not from this sensor");
    }
