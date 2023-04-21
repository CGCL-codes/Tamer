    public LUT getChannelLut(int channel) {
        int channels = getNChannels();
        if (lut == null) setupLuts(channels);
        if (channel < 1 || channel > lut.length) throw new IllegalArgumentException("Channel out of range");
        return lut[channel - 1];
    }
