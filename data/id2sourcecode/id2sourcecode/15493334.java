    public void sendAllNotesOff() {
        if (getChannels() != null) {
            for (int channel = 0; channel < getChannels().length; channel++) {
                sendControlChange(channel, MidiControllers.ALL_NOTES_OFF, 0);
            }
        }
    }
