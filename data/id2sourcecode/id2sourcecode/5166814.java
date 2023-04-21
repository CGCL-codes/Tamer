    int getChannelIndex() {
        int channels = getNChannels();
        if (lut == null) setupLuts(channels);
        int index = getChannel() - 1;
        return index;
    }
