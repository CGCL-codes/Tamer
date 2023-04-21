    @Override
    public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (AudioFormats.matches(sourceFormat, targetFormat)) {
            return sourceStream;
        }
        targetFormat = replaceNotSpecified(sourceFormat, targetFormat);
        if (targetFormat.getSampleRate() != AudioSystem.NOT_SPECIFIED && sourceFormat.getSampleRate() != AudioSystem.NOT_SPECIFIED && targetFormat.getChannels() != AudioSystem.NOT_SPECIFIED && sourceFormat.getChannels() != AudioSystem.NOT_SPECIFIED && targetFormat.getSampleSizeInBits() != AudioSystem.NOT_SPECIFIED && sourceFormat.getSampleSizeInBits() != AudioSystem.NOT_SPECIFIED && isConversionSupported(targetFormat, sourceFormat)) {
            return new SampleRateConverterStream(sourceStream, targetFormat);
        }
        throw new IllegalArgumentException("format conversion not supported");
    }
