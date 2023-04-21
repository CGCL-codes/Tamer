    public void setDisplayRange(double min, double max) {
        ip.setMinAndMax(min, max);
        int c = getChannelIndex();
        lut[c].min = min;
        lut[c].max = max;
    }
