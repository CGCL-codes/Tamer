    protected void checkSynthesizer(Synthesizer synth) throws Exception {
        MidiChannel[] channels;
        synth.open();
        try {
            channels = synth.getChannels();
            assertNotNull(constructErrorMessage(synth, "getChannels() result null", true), channels);
            int numChannels = channels.length;
            assertTrue(constructErrorMessage(synth, "getChannels() result has wrong length", true), numChannels == 16);
            for (int i = 0; i < channels.length; i++) {
                assertNotNull(constructErrorMessage(synth, "getChannels() result element null", true), channels[i]);
            }
        } finally {
            synth.close();
        }
    }
