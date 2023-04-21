    public boolean isConversionSupported(AudioFormat targetFormat, AudioFormat sourceFormat) {
        targetFormat = replaceNotSpecified(sourceFormat, targetFormat);
        boolean res = AudioFormats.matches(sourceFormat, targetFormat) || (doMatch(targetFormat.getFrameRate(), sourceFormat.getFrameRate()) && doMatch(targetFormat.getSampleRate(), sourceFormat.getSampleRate()) && getConversionType(getFormatType(sourceFormat), sourceFormat.getChannels(), getFormatType(targetFormat), targetFormat.getChannels()) != CONVERT_NOT_POSSIBLE);
        if (TDebug.TraceAudioConverter) {
            TDebug.out(">PCM2PCM: isConversionSupported(AudioFormat, AudioFormat):");
            TDebug.out("checking if conversion possible");
            TDebug.out("from: " + sourceFormat);
            TDebug.out("to  : " + targetFormat);
            TDebug.out("< result : " + res);
        }
        return res;
    }
