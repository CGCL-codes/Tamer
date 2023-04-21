    public double getDisplayRangeMax() {
        if (lut != null) return lut[getChannelIndex()].max; else return 255.0;
    }
