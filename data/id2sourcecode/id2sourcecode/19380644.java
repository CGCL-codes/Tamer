    void addState(EEGChannelValue value) {
        getValues().add(new EEGValues(value.getMillisFromStart(), value.getChannelStrength()));
    }
