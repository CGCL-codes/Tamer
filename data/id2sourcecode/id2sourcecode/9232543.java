    private void checkChannelPressure(Synthesizer synth, MidiChannel channel, int nPressure) {
        channel.setChannelPressure(nPressure);
        int value = channel.getChannelPressure();
        assertTrue(constructErrorMessage(synth, "channel pressure", true), nPressure == value || value == 0);
    }
