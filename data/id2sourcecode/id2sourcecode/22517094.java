    public MidiChordPlayer() {
        Synthesizer synth = SynthManager.getSynthesizer();
        for (int midichannel = 0; midichannel < 16; midichannel++) {
            channel = synth.getChannels()[midichannel];
            if (channel != null) {
                break;
            }
            if (midichannel == 15 && channel == null) {
            }
        }
    }
