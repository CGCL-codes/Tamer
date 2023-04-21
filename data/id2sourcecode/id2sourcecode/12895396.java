    public Channel[] getChannels() {
        ArrayList<Channel> channels = new ArrayList<Channel>();
        for (short i = 1; i <= maxChannel; i++) {
            short value = getChannelValue(i);
            if (value != 0) channels.add(new Channel(i, value));
        }
        return channels.toArray(new Channel[0]);
    }
