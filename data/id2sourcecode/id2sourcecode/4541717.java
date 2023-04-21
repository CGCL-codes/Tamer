    public Channel getMaxChannel() {
        double[] rgbValues = getValues();
        int maxIndex = Maths.maxIndex(rgbValues);
        return Channel.getChannelByArrayIndex(maxIndex);
    }
