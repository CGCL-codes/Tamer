    private static final AudioFormat createTargetFormat(AudioFormat src, AudioFormat.Encoding dst) {
        return new AudioFormat(dst, src.getSampleRate(), 8, src.getChannels(), src.getChannels(), src.getSampleRate(), false);
    }
