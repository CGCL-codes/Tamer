    public Channel[] getAllChannels() {
        Channel[] channels = new Channel[maxChannel];
        for (short i = 0; i < maxChannel; i++) channels[i] = new Channel((short) (i + 1), getChannelValue((short) (i + 1)));
        return channels;
    }
