    public Channel[] getChannelsForCue() {
        int cnt = 0;
        for (short i = 1; i <= maxChannel; i++) {
            if (getChannelValue(i) != 0 || faderValues[i] == -100) cnt++;
        }
        Channel[] channels = new Channel[cnt];
        cnt = 0;
        for (short i = 1; i <= maxChannel; i++) {
            short value = getChannelValue(i);
            if (value != 0 || faderValues[i] == -100) {
                if (faderValues[i] == -100) channels[cnt] = new Channel(i, (short) -100); else channels[cnt] = new Channel(i, value);
                cnt++;
            }
        }
        return channels;
    }
