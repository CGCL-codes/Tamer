    public Channel getMinChannel() {
        double[] rgbValues = getValues();
        int minIndex = Maths.minIndex(rgbValues);
        return Channel.getChannelByArrayIndex(minIndex);
    }
