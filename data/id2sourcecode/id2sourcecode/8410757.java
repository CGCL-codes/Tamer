    public AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding, AudioFormat sourceFormat) {
        if (TDebug.TraceAudioConverter) {
            TDebug.out(">MP3Lame getTargetFormats(AudioFormat.Encoding, AudioFormat):");
            TDebug.out("checking out possible target formats");
            TDebug.out("from: " + sourceFormat);
            TDebug.out("to  : " + targetEncoding);
        }
        if (isConversionSupported(targetEncoding, sourceFormat)) {
            AudioFormatSet result = new AudioFormatSet();
            Iterator iterator = getCollectionTargetFormats().iterator();
            while (iterator.hasNext()) {
                AudioFormat targetFormat = (AudioFormat) iterator.next();
                if (doMatch(targetFormat.getSampleRate(), sourceFormat.getSampleRate()) && targetFormat.getEncoding().equals(targetEncoding) && doMatch(targetFormat.getChannels(), sourceFormat.getChannels())) {
                    targetFormat = getDefaultTargetFormat(targetFormat, sourceFormat, true);
                    result.add(targetFormat);
                }
            }
            if (TDebug.TraceAudioConverter) {
                TDebug.out("<found " + result.size() + " matching formats.");
            }
            return result.toAudioFormatArray();
        } else {
            if (TDebug.TraceAudioConverter) {
                TDebug.out("<returning empty array.");
            }
            return EMPTY_FORMAT_ARRAY;
        }
    }
