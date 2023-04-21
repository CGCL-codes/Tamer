    private void checkMethod(Synthesizer synth, String strMethodName, boolean bExceptionExpected, boolean bOpen) throws Exception {
        try {
            if ("getMaxPolyphony()".equals(strMethodName)) synth.getMaxPolyphony(); else if ("getLatency()".equals(strMethodName)) synth.getLatency(); else if ("getChannels()".equals(strMethodName)) synth.getChannels(); else if ("getVoiceStatus()".equals(strMethodName)) synth.getVoiceStatus(); else if ("getDefaultSoundbank()".equals(strMethodName)) synth.getDefaultSoundbank(); else if ("getAvailableInstruments()".equals(strMethodName)) synth.getAvailableInstruments(); else if ("getLoadedInstruments()".equals(strMethodName)) synth.getLoadedInstruments(); else throw new RuntimeException("unknown method name");
            if (bExceptionExpected) {
                fail(constructErrorMessage(synth, strMethodName, bExceptionExpected, bOpen));
            }
        } catch (IllegalStateException e) {
            if (!bExceptionExpected) {
                fail(constructErrorMessage(synth, strMethodName, bExceptionExpected, bOpen));
            }
        }
    }
