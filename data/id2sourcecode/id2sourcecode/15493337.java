    public void sendControlChange(int channel, int controller, int value) {
        if (getChannels() != null && channel >= 0 && channel < getChannels().length) {
            getChannels()[channel].controlChange(controller, value);
        }
    }
