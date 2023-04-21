    public static void ejemplo1() throws MidiUnavailableException, InterruptedException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        MidiChannel[] channels = synthesizer.getChannels();
        channels[0].noteOn(60, 93);
        Thread.sleep(2000);
        channels[0].noteOff(60);
        synthesizer.close();
    }
