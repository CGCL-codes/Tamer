    public void sendPitchBend(int channel, int value) {
        if (getChannels() != null && channel >= 0 && channel < getChannels().length) {
            getChannels()[channel].setPitchBend((value * 128));
        }
    }
