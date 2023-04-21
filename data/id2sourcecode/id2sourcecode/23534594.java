    private AudioFormat[] getIntermediateFormats(AudioFormat sourceFormat, AudioFormat targetFormat) {
        AudioFormat.Encoding sourceEncoding = sourceFormat.getEncoding();
        AudioFormat.Encoding targetEncoding = targetFormat.getEncoding();
        blockCurrentThread();
        boolean bDirectConversionPossible = AudioSystem.isConversionSupported(targetFormat, sourceFormat);
        unblockCurrentThread();
        if (bDirectConversionPossible) {
            return EMPTY_FORMAT_ARRAY;
        } else if (isPCM(sourceEncoding) && isPCM(targetEncoding)) {
            return null;
        } else if (!isPCM(sourceEncoding)) {
            AudioFormat intermediateFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), AudioSystem.NOT_SPECIFIED, sourceFormat.getSampleRate(), true);
            blockCurrentThread();
            AudioFormat[] aPreIntermediateFormats = getIntermediateFormats(sourceFormat, intermediateFormat);
            unblockCurrentThread();
            AudioFormat[] aPostIntermediateFormats = getIntermediateFormats(intermediateFormat, targetFormat);
            if (aPreIntermediateFormats != null && aPostIntermediateFormats != null) {
                AudioFormat[] aIntermediateFormats = new AudioFormat[aPreIntermediateFormats.length + 1 + aPostIntermediateFormats.length];
                System.arraycopy(aPreIntermediateFormats, 0, aIntermediateFormats, 0, aPreIntermediateFormats.length);
                aIntermediateFormats[aPreIntermediateFormats.length] = intermediateFormat;
                System.arraycopy(aPostIntermediateFormats, 0, aIntermediateFormats, aPreIntermediateFormats.length, aPostIntermediateFormats.length);
                return aIntermediateFormats;
            } else {
                return null;
            }
        } else if (!isPCM(targetEncoding)) {
            AudioFormat intermediateFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, targetFormat.getSampleRate(), targetFormat.getSampleSizeInBits(), targetFormat.getChannels(), AudioSystem.NOT_SPECIFIED, targetFormat.getSampleRate(), true);
            AudioFormat[] aPreIntermediateFormats = getIntermediateFormats(sourceFormat, intermediateFormat);
            blockCurrentThread();
            AudioFormat[] aPostIntermediateFormats = getIntermediateFormats(intermediateFormat, targetFormat);
            unblockCurrentThread();
            if (aPreIntermediateFormats != null && aPostIntermediateFormats != null) {
                AudioFormat[] aIntermediateFormats = new AudioFormat[aPreIntermediateFormats.length + 1 + aPostIntermediateFormats.length];
                System.arraycopy(aPreIntermediateFormats, 0, aIntermediateFormats, 0, aPreIntermediateFormats.length);
                aIntermediateFormats[aPreIntermediateFormats.length] = intermediateFormat;
                System.arraycopy(aPostIntermediateFormats, 0, aIntermediateFormats, aPreIntermediateFormats.length, aPostIntermediateFormats.length);
                return aIntermediateFormats;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
