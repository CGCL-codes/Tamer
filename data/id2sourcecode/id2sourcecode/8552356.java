    public SoftVoice(SoftSynthesizer synth) {
        synthesizer = synth;
        filter_left = new SoftFilter(synth.getFormat().getSampleRate());
        filter_right = new SoftFilter(synth.getFormat().getSampleRate());
        nrofchannels = synth.getFormat().getChannels();
    }
