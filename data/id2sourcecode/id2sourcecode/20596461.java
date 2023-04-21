    private AudioInputStream convertToEqualsSampleRate(AudioInputStream stream, float sampleRate) {
        AudioFormat formatWithNeedeedSampleRate = new AudioFormat(stream.getFormat().getEncoding(), sampleRate, stream.getFormat().getSampleSizeInBits(), stream.getFormat().getChannels(), stream.getFormat().getFrameSize(), sampleRate, stream.getFormat().isBigEndian());
        return AudioSystem.getAudioInputStream(formatWithNeedeedSampleRate, stream);
    }
