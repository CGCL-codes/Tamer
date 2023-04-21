    public Channel[] getAllChannels() {
        Channel[] channels = new Channel[maxChannel];
        for (int i = 0; i < maxChannel; i++) channels[i] = getChannel((short) (i + 1));
        return channels;
    }
