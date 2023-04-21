    public EEGChannelState getChannelState(FrequencyType frequencyType) {
        if (log.isDebugEnabled()) {
            log.debug("getChannelState looking for " + frequencyType.name());
        }
        for (EEGChannelState state : channelStates) {
            if (state.getFrequencyType().equals(frequencyType)) {
                if (log.isDebugEnabled()) {
                    log.debug("getChannelState returning" + frequencyType.name());
                }
                return state;
            }
        }
        return null;
    }
