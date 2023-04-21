    public static AudioFormat applyPropertiesToFormat(AudioFormat af, Map<String, Object> props) {
        return new AudioFormat(af.getEncoding(), af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels(), af.getFrameSize(), af.getFrameRate(), af.isBigEndian(), props);
    }
