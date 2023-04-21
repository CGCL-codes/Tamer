    protected void setChannel(FrequencyType frequencyType, double strength) {
        EEGChannelState state = getChannelState(frequencyType);
        if (state != null) {
            setChannelValue(getChannelValue(frequencyType), state, strength);
        }
    }
