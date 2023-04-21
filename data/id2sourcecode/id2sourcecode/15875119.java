    public void resetAllChannels(int source) {
        Channel[] channels = channelValues.getChannels(source);
        Channel[] result = new Channel[channels.length];
        for (int i = 0; i < channels.length; i++) result[i] = new Channel(channels[i].address, (short) -100);
        setChannelValues(result, source);
    }
