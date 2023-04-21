    public MixProcess(AudioMixerStrip strip, MixVariables vars) {
        if (strip == null) {
            throw new IllegalArgumentException("null strip to route to");
        }
        routedStrip = strip;
        this.vars = vars;
        ChannelFormat format = vars.getChannelFormat();
        channelGains = new float[format.getCount()];
        smoothedChannelGains = new float[format.getCount()];
    }
