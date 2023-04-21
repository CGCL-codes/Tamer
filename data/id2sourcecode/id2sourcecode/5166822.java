    public void resetDisplayRange() {
        ip.resetMinAndMax();
        int c = getChannelIndex();
        lut[c].min = ip.getMin();
        lut[c].max = ip.getMax();
    }
