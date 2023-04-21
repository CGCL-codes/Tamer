    public Channel[] getChannels(short[] addresses) {
        Channel[] c = new Channel[addresses.length];
        for (int i = 0; i < addresses.length; i++) c[i] = new Channel(addresses[i], getChannelValue(addresses[i]));
        return c;
    }
