    public boolean isConversionSupported(AudioFormat targetFormat, AudioFormat sourceFormat) {
        if (AudioFloatConverter.getConverter(sourceFormat) == null) return false;
        if (AudioFloatConverter.getConverter(targetFormat) == null) return false;
        if (sourceFormat.getChannels() <= 0) return false;
        if (targetFormat.getChannels() <= 0) return false;
        return true;
    }
