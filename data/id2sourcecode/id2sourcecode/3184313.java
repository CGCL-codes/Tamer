    public float getAutoscaleYLength(int xOffset, int xLength) {
        return 2 * getChannelModel().getMaxSampleValue(xOffset, xLength);
    }
