    public double getDisplayRangeMin() {
        if (lut != null) return lut[getChannelIndex()].min; else return 0.0;
    }
