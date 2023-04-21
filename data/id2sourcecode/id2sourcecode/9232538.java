    protected void checkSynthesizer(Synthesizer synth) throws Exception {
        MidiChannel channel;
        synth.open();
        try {
            channel = synth.getChannels()[0];
            checkNotes(synth, channel);
            checkNotes2(synth, channel);
            checkPolyPressure(synth, channel);
            checkChannelPressure(synth, channel);
            checkProgramChange(synth, channel);
            checkProgramChange2(synth, channel);
            checkPitchbend(synth, channel);
        } finally {
            synth.close();
        }
    }
