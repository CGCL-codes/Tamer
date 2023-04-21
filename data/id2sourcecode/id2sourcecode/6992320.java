    public EEGChannelState getChannelState(FrequencyType frequencyType) {
        for (EEGChannelState state : channelStates) {
            if (state.getFrequencyType().equals(frequencyType)) {
                return state;
            }
        }
        return null;
    }
