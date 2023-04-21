    private void checkMessage2(Type type) throws Exception {
        Synthesizer synth = new TestSynthesizer();
        synth.open();
        TestSynthesizer.TestChannel[] channels = (TestSynthesizer.TestChannel[]) synth.getChannels();
        try {
            Receiver r = synth.getReceiver();
            checkMessage(type, channels, r, 0, 17, 55);
            checkMessage(type, channels, r, 15, 0, 0);
            checkMessage(type, channels, r, 5, 127, 127);
        } finally {
            synth.close();
        }
    }
