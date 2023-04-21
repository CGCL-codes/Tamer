    public Channel[] getChannels(short[] addresses) {
        Channel[] channels = new Channel[addresses.length];
        for (int i = 0; i < addresses.length; i++) channels[i] = getChannel(addresses[i]);
        return channels;
    }
