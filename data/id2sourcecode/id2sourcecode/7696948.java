    @Override
    public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (AudioFormats.matches(sourceFormat, targetFormat)) {
            return sourceStream;
        }
        if (doMatch(targetFormat.getFrameRate(), sourceFormat.getFrameRate()) && doMatch(targetFormat.getChannels(), sourceFormat.getChannels())) {
            if (doMatch(targetFormat.getSampleSizeInBits(), 8)) {
                if (targetFormat.getEncoding().equals(ULAW)) {
                    return new ToUlawStream(sourceStream);
                } else if (targetFormat.getEncoding().equals(ALAW)) {
                    return new ToAlawStream(sourceStream);
                }
            }
        }
        throw new IllegalArgumentException("format conversion not supported");
    }
