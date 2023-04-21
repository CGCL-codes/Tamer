    public AudioMixerStrip(AudioMixer mixer, AudioControlsChain controlsChain) {
        super(controlsChain);
        this.mixer = mixer;
        buffer = createBuffer();
        channelFormat = buffer.getChannelFormat();
    }
