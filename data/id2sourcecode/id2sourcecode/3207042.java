    public JavaSoundDevice(Mixer mixer, AudioFormat af, DataLine.Info info, int bufferSizeInFrames) {
        this.mixer = mixer;
        this.af = af;
        this.info = info;
        this.line = null;
        bytesPerFrame = 2 * af.getChannels();
        byteBuffer = new byte[bufferSizeInFrames * bytesPerFrame];
    }
