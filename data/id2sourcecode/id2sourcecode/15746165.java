    public float[] getChannelData(int channel) throws BadParameterException {
        if (channel >= numberOfChannels || channel < 0) throw new BadParameterException("Channel out of range");
        return data[channel];
    }
