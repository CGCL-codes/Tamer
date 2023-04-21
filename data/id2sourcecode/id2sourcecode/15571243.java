    private AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding, AudioFormat sourceFormat, boolean notSpecifiedOK) {
        boolean bitSizeOK = isBitSizeOK(sourceFormat, notSpecifiedOK);
        boolean channelsOK = isChannelsOK(sourceFormat, notSpecifiedOK);
        if (HAS_ENCODING && bitSizeOK && channelsOK && !sourceFormat.isBigEndian() && sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && targetEncoding.equals(FlacEncoding.FLAC)) {
            if (DEBUG) {
                System.out.println("FLAC converter: can encode: " + sourceFormat + " to " + targetEncoding);
            }
            AudioFormat[] formats = { new AudioFormat(FlacEncoding.FLAC, sourceFormat.getSampleRate(), -1, sourceFormat.getChannels(), -1, -1, false) };
            return formats;
        } else if (bitSizeOK && channelsOK && sourceFormat.getEncoding().equals(FlacEncoding.FLAC) && targetEncoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            if (DEBUG) {
                System.out.println("FLAC converter: can decode: " + sourceFormat + " to " + targetEncoding);
            }
            AudioFormat[] formats = { new AudioFormat(sourceFormat.getSampleRate(), sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), true, false) };
            return formats;
        } else {
            if (DEBUG) {
                System.out.println("FLAC converter: cannot de/encode: " + sourceFormat + " to " + targetEncoding);
            }
            AudioFormat[] formats = {};
            return formats;
        }
    }
