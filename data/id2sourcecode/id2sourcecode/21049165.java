    protected void setSoundVolume(float volume) {
        if (this.sequencer == null) {
            return;
        }
        MidiChannel[] channels = ((Synthesizer) this.sequencer).getChannels();
        for (int i = 0; i < channels.length; i++) {
            channels[i].controlChange(MidiRenderer.GAIN_CONTROLLER, (int) (volume * 127));
        }
    }
