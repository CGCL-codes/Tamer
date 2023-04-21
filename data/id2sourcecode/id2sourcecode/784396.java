    private static AudioInputStream convertSampleSizeAndEndianess(int nSampleSizeInBits, boolean bBigEndian, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(sourceFormat.getEncoding(), sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), calculateFrameSize(sourceFormat.getChannels(), nSampleSizeInBits), sourceFormat.getFrameRate(), bBigEndian);
        return AudioSystem.getAudioInputStream(targetFormat, sourceStream);
    }
