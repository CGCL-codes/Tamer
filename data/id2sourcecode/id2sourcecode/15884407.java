    public MusicPlugin() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                }
            }
            synthesizer.open();
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, 10);
        } catch (Exception ex) {
        }
        if (synthesizer != null) {
            Soundbank sb = synthesizer.getDefaultSoundbank();
            if (sb != null) {
                instruments = synthesizer.getDefaultSoundbank().getInstruments();
                synthesizer.loadInstrument(instruments[0]);
            }
            channels = synthesizer.getChannels();
            channel = channels[0];
        }
    }
