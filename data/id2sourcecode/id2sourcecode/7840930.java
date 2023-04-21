    public boolean equals(Object o) {
        if (o == null || !(o instanceof EEGChannelValue)) {
            return false;
        }
        EEGChannelValue value = (EEGChannelValue) o;
        return value.getChannelState().equals(getChannelState());
    }
