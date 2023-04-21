    private static final AudioFormat createTargetFormat(AudioFormat src, AudioFormat dst) {
        return new AudioFormat(dst.getEncoding(), src.getSampleRate(), dst.getSampleSizeInBits(), src.getChannels(), dst.getSampleSizeInBits() * src.getChannels() / 8, src.getFrameRate(), dst.isBigEndian());
    }
