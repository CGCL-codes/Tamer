    public static AudioFormat getAiffCompatibleAudioFormat(AudioFormat af) {
        return new AudioFormat((af.getSampleSizeInBits() == 8) ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED, af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels(), af.getFrameSize(), af.getFrameRate(), true, af.properties());
    }
