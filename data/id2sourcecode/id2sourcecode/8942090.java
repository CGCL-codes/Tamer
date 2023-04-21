    private MusicGenerator() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            midiChannel = synth.getChannels()[0];
            receiver = synth.getReceiver();
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
        }
    }
