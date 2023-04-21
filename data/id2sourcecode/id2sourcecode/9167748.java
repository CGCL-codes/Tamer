    public void play() {
        if (sequence == null) {
            throw new IllegalStateException("MidiAsset.play: " + "Midi sequence not loaded");
        }
        if (sequencer == null) {
            initialiseSequencer();
        }
        try {
            sequencer.setSequence(sequence);
            if (continuallyPlay) {
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            }
            sequencer.start();
            channels = synthesizer.getChannels();
            if (volume != -1) {
                setVolume(volume);
            }
        } catch (InvalidMidiDataException exception) {
            throw new IllegalStateException("MidiAsset.play: " + "Cannot play Midi sequence.");
        }
    }
