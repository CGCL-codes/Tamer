    public void setVolume(float volume) {
        if (sequencer == null || this.volume == volume) {
            this.volume = volume;
            return;
        }
        if (isVolumeSupported() == false) {
            Utility.error("Setting midi volume not supported!", "MidiCore.setVolume(float)");
            return;
        }
        this.volume = volume;
        MidiChannel[] channels = ((Synthesizer) sequencer).getChannels();
        for (int i = 0; i < channels.length; i++) {
            channels[i].controlChange(GAIN_CONTROLLER, (int) (volume * 127));
        }
    }
