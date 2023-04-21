    protected AudioFormat getDefaultTargetFormat(AudioFormat targetFormat, AudioFormat sourceFormat) {
        if (!LIB_AVAILABLE) {
            throw new IllegalArgumentException("format conversion not supported: ogg/vorbis native library not found.");
        }
        if (TDebug.TraceAudioConverter) {
            TDebug.out("VorbisFormatConversionProvider.getDefaultTargetFormat(): target format: " + targetFormat);
        }
        if (TDebug.TraceAudioConverter) {
            TDebug.out("VorbisFormatConversionProvider.getDefaultTargetFormat(): source format: " + sourceFormat);
        }
        AudioFormat newTargetFormat = null;
        for (AudioFormat format : getCollectionTargetFormats()) {
            if (AudioFormats.matches(targetFormat, format)) {
                newTargetFormat = format;
            }
        }
        if (newTargetFormat == null) {
            throw new IllegalArgumentException("conversion not supported");
        }
        if (TDebug.TraceAudioConverter) {
            TDebug.out("VorbisFormatConversionProvider.getDefaultTargetFormat(): new target format: " + newTargetFormat);
        }
        newTargetFormat = new AudioFormat(targetFormat.getEncoding(), sourceFormat.getSampleRate(), newTargetFormat.getSampleSizeInBits(), newTargetFormat.getChannels(), newTargetFormat.getFrameSize(), sourceFormat.getSampleRate(), newTargetFormat.isBigEndian(), targetFormat.properties());
        if (TDebug.TraceAudioConverter) TDebug.out("VorbisFormatConversionProvider.getDefaultTargetFormat(): really new target format: " + newTargetFormat);
        return newTargetFormat;
    }
