    private void setChannels() {
        for (EEGChannelState state : getChannelStates()) {
            setChannelValue(state);
        }
    }
