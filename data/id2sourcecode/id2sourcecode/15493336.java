    public void sendNoteOff(int channel, int key, int velocity) {
        if (getChannels() != null && channel >= 0 && channel < getChannels().length) {
            getChannels()[channel].noteOff(key, velocity);
        }
    }
