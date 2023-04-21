    public void sendNoteOn(int channel, int key, int velocity) {
        if (getChannels() != null && channel >= 0 && channel < getChannels().length) {
            getChannels()[channel].noteOn(key, velocity);
        }
    }
