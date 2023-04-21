    static void initSynthesizer() {
        if (synthesizer == null) {
            System.out.println("init EasyMIDI");
            try {
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
                instruments = synthesizer.getDefaultSoundbank().getInstruments();
                int c = openNote(0, 60, 0);
                Thread.sleep(100);
                closeNote(60, c);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            if (drumChannel == null) {
                drumChannel = synthesizer.getChannels()[9];
            }
        }
    }
