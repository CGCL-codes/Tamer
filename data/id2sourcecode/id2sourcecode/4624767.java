    @Override
    public boolean isConversionSupported(AudioFormat targetFormat, AudioFormat sourceFormat) {
        boolean result = (targetFormat.getSampleRate() == AudioSystem.NOT_SPECIFIED || targetFormat.getSampleRate() == AudioSystem.NOT_SPECIFIED || !doMatch(targetFormat.getSampleRate(), sourceFormat.getSampleRate()) && doMatch(targetFormat.getChannels(), sourceFormat.getChannels())) && AudioUtils.containsFormat(sourceFormat, getCollectionSourceFormats().iterator()) && AudioUtils.containsFormat(targetFormat, getCollectionTargetFormats().iterator());
        if (TDebug.TraceAudioConverter) {
            TDebug.out(">SampleRateConverter: isConversionSupported(AudioFormat, AudioFormat):");
            TDebug.out("checking if conversion possible");
            TDebug.out("from: " + sourceFormat);
            TDebug.out("to  : " + targetFormat);
            TDebug.out("< result : " + result);
        }
        return result;
    }
