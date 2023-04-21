    public void addChannelValue(EEGChannelValue value) {
        EEGSessionData data = getSession().getSessionDataForState(value.getChannelState());
        data.addState(value);
    }
