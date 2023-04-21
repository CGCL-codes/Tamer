    private MidiChannel[] getChannels() {
        if (this.channels == null && this.port.getSynth() != null) {
            this.channels = this.port.getSynth().getChannels();
        }
        return this.channels;
    }
