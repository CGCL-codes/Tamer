    public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (AudioFormats.matches(sourceFormat, targetFormat)) {
            return sourceStream;
        }
        if (doMatch(targetFormat.getFrameRate(), sourceFormat.getFrameRate()) && doMatch(targetFormat.getSampleRate(), sourceFormat.getSampleRate())) {
            targetFormat = replaceNotSpecified(sourceFormat, targetFormat);
            int sourceType = getFormatType(sourceFormat);
            int targetType = getFormatType(targetFormat);
            int conversionType = getConversionType(sourceType, sourceFormat.getChannels(), targetType, targetFormat.getChannels());
            if (TDebug.TraceAudioConverter) {
                TDebug.out("PCM2PCM: sourceType=" + formatType2Str(sourceType) + ", " + sourceFormat.getChannels() + "ch" + " targetType=" + formatType2Str(targetType) + ", " + targetFormat.getChannels() + "ch" + " conversionType=" + conversionType2Str(conversionType));
            }
            if (conversionType == CONVERT_NOT_POSSIBLE) {
                throw new IllegalArgumentException("format conversion not supported");
            }
            return new PCM2PCMStream(sourceStream, targetFormat, conversionType);
        }
        throw new IllegalArgumentException("format conversion not supported");
    }
