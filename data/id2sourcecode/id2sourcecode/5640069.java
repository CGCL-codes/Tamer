    private void open() {
        try {
            sequencer = MidiSystem.getSequencer();
            if (sequencer instanceof Synthesizer) {
                synthesizer = (Synthesizer) sequencer;
                channels = synthesizer.getChannels();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        sequencer.addMetaEventListener(this);
    }
