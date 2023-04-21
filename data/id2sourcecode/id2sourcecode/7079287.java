    private void checkMethods(Synthesizer synth, boolean bOpen) throws Exception {
        boolean bExpectingException = false;
        checkMethod(synth, "getMaxPolyphony()", bExpectingException, bOpen);
        checkMethod(synth, "getLatency()", bExpectingException, bOpen);
        checkMethod(synth, "getChannels()", bExpectingException, bOpen);
        checkMethod(synth, "getVoiceStatus()", bExpectingException, bOpen);
        checkMethod(synth, "getDefaultSoundbank()", bExpectingException, bOpen);
        checkMethod(synth, "getAvailableInstruments()", bExpectingException, bOpen);
        checkMethod(synth, "getLoadedInstruments()", bExpectingException, bOpen);
    }
