    public static AudioInputStream convert(float sampleRate, AudioInputStream inputStream) throws IllegalArgumentException {
        AudioFormat format = new AudioFormat(inputStream.getFormat().getEncoding(), sampleRate, inputStream.getFormat().getSampleSizeInBits(), inputStream.getFormat().getChannels(), inputStream.getFormat().getFrameSize(), sampleRate, inputStream.getFormat().isBigEndian());
        AudioInputStream result = AudioSystem.getAudioInputStream(format, inputStream);
        return result;
    }
