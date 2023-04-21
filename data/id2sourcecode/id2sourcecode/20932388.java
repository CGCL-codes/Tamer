    public void setChannel(int channel) {
        wCount = hCount = 0;
        maxValue = colorSpace.getMaxValue(channel);
        div = colorSpace.getVerticalSubSampleFactor(channel);
        accChannel = colorSpace.getChannelOffset(channel);
    }
