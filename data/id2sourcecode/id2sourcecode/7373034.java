    public Channel[] getChannels(short[] addresses) {
        Channel[] result = new Channel[addresses.length];
        for (int i = 0; i < result.length; i++) result[i] = getChannel(addresses[i]);
        return result;
    }
