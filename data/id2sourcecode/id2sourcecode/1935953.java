    public Channel[] getChannels(short[] addresses) {
        Channel[] channels = new Channel[addresses.length];
        for (int i = 0; i < channels.length; i++) channels[i] = new Channel(addresses[i], getChannelValue(addresses[i]));
        return channels;
    }
