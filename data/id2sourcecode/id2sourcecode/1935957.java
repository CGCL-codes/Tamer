    public Channel[] getChannels() {
        int cnt = 0;
        for (short i = 1; i <= maxChannel; i++) {
            if (getChannelValue(i) != 0) cnt++;
        }
        Channel[] channels = new Channel[cnt];
        cnt = 0;
        for (short i = 1; i <= maxChannel; i++) {
            short value = getChannelValue(i);
            if (value != 0) {
                channels[cnt] = new Channel(i, value);
                cnt++;
            }
        }
        return channels;
    }
