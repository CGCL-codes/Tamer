    public static AudioFormat channelFormat(AudioFormat f, int n) {
        int channels = f.getChannels();
        return new AudioFormat(f.getEncoding(), f.getSampleRate(), f.getSampleSizeInBits(), n, n * f.getFrameSize() / channels, f.getFrameRate(), f.isBigEndian());
    }
