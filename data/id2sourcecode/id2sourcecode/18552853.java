    public void setSoundVolume(int volume) {
        if (this.sequencer == null) {
            return;
        }
        if (volumeSupported) {
            MidiChannel[] channels = ((Synthesizer) this.sequencer).getChannels();
            for (int i = 0; i < channels.length; i++) {
                channels[i].controlChange(GAIN_CONTROLLER, volume);
            }
        }
    }
