    @Override
    public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (AudioFormats.matches(sourceFormat, targetFormat)) {
            return sourceStream;
        }
        if (doMatch(targetFormat.getFrameRate(), sourceFormat.getFrameRate()) && doMatch(targetFormat.getChannels(), sourceFormat.getChannels())) {
            if (doMatch(sourceFormat.getSampleSizeInBits(), 8)) {
                if (sourceFormat.getEncoding().equals(ULAW)) {
                    return new FromUlawStream(sourceStream, targetFormat);
                } else if (sourceFormat.getEncoding().equals(ALAW)) {
                    return new FromAlawStream(sourceStream, targetFormat);
                }
            }
        }
        throw new IllegalArgumentException("format conversion not supported");
    }
