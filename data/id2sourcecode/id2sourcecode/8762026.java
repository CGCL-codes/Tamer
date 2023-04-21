    public ChannelCalibration getChannelCalibration(int channel) {
        checkChannel(channel);
        return chCalibration[channel - 1];
    }
