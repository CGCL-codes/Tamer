    public void setModule(Module mod) {
        module = mod;
        setTempo(module.getTempo());
        setBpm(module.getBpm());
        channel = new Channel[module.getChannelCount()];
        for (int i = 0; i < channel.length; i++) {
            channel[i] = new Channel(this);
        }
        order = 0;
        playingPattern = module.getPatterns()[module.getPatternOrder()[order]];
        ;
    }
