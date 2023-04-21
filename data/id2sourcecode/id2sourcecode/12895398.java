    public Channel[] getChannelsForCue() {
        ArrayList<Channel> channels = new ArrayList<Channel>();
        IValueSet faderSet = channelSets.getSet((int) FADER_SOURCE);
        for (short i = 1; i <= maxChannel; i++) {
            IValueSet[] sets = channelSets.getHighPrioritySets(i);
            short value = 0;
            short len = 0;
            for (IValueSet s : sets) {
                if (s.getChannelValue(i) >= 0) {
                    value += s.getChannelValue(i);
                    len++;
                }
            }
            if (faderSet.getChannelValue(i) == -100) value = -100; else if (len == 0) value = -100; else value /= len;
            if (value != 0) channels.add(new Channel(i, value));
        }
        return channels.toArray(new Channel[0]);
    }
