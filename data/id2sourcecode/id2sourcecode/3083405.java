    public static AudioInputStream convertSampleRate(AudioInputStream audioInputStream, float sampleRate) {
        AudioFormat sourceFormat = audioInputStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(sourceFormat.getEncoding(), sampleRate, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), sourceFormat.getFrameSize(), sampleRate, sourceFormat.isBigEndian());
        return AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
    }
