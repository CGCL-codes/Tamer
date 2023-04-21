    private void initialize() throws Exception {
        defSynth = MidiSystem.getSynthesizer();
        defSynth.open();
        defSynth.loadAllInstruments(MidiSystem.getSoundbank(new File("soundbank.gm")));
        defSynthChannels = defSynth.getChannels();
        defSeq = MidiSystem.getSequencer();
        defSeq.open();
        try {
            defTrns = MidiSystem.getTransmitter();
        } catch (Exception e) {
            System.out.println("Was not able to get handle to MIDI device.");
            System.out.println("Disabling MIDI input.");
            defTrns = new MockupMIDITransmitter();
        }
    }
