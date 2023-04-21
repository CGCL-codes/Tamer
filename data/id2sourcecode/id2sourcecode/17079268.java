    public void setVolume(float volume) {
        this.volume = volume;
        MidiChannel[] channels = SynthManager.getSynthesizer().getChannels();
        int max = 255;
        for (int i = 0; i < channels.length; i++) {
            channels[i].controlChange(7, Math.round(volume * max));
        }
    }
