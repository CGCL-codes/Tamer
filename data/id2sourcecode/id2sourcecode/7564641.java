    public void setAudioFormat(AudioFormat audioFormat) throws LineUnavailableException {
        if (audioFormat.getChannels() != 2) throw new IllegalArgumentException("Only support two channel formats.");
        this.audioFormat = audioFormat;
        line.open(audioFormat, lineBufferSize);
        channelFormat = new AudioFormat(audioFormat.getEncoding(), audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), 1, audioFormat.getFrameSize() / 2, audioFormat.getFrameRate(), audioFormat.isBigEndian());
        tone = new ToneGenerator(channelFormat.getFrameRate(), 440, 16384);
    }
