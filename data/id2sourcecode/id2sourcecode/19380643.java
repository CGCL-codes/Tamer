    public boolean isForValue(EEGChannelValue value) {
        return value.getChannelState().equals(getState());
    }
