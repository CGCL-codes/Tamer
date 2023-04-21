    public DataList getChannel(int ch) {
        if (ch < 0 || ch >= nChannels) throw new IllegalArgumentException("Channel " + ch + " does not exist");
        return samples[ch];
    }
