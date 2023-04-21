    private static AudioFormat replaceSampleRate(AudioFormat format, float newSampleRate) {
        if (format.getSampleRate() == newSampleRate) {
            return format;
        }
        return new AudioFormat(format.getEncoding(), newSampleRate, format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), newSampleRate, format.isBigEndian());
    }
