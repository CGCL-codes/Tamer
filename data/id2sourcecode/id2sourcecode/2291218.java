    public void testPitchbend() throws Exception {
        Synthesizer synth = new TestSynthesizer();
        synth.open();
        TestSynthesizer.TestChannel[] channels = (TestSynthesizer.TestChannel[]) synth.getChannels();
        try {
            Receiver r = synth.getReceiver();
            checkPitchbend(channels, r, 0, 0);
            checkPitchbend(channels, r, 5, 127);
            checkPitchbend(channels, r, 7, 128);
            checkPitchbend(channels, r, 15, 16383);
        } finally {
            synth.close();
        }
    }
