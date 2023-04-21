    public void sendProgramChange(int channel, int value) {
        if (getChannels() != null && channel >= 0 && channel < getChannels().length) {
            getChannels()[channel].programChange(value);
        }
    }
