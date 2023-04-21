    public static void playNote(int midiValue) throws MidiUnavailableException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        if (!(synth.isOpen())) {
            try {
                synth.open();
            } catch (MidiUnavailableException e) {
                throw new MidiUnavailableException("Midi unavailable");
            }
        }
        MidiChannel chan[] = synth.getChannels();
        if (chan[4] != null) {
            chan[4].noteOn(midiValue, NOTE_VELOCITY);
        }
    }
