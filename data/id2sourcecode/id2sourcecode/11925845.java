    public static int getEsdFormat(AudioFormat audioFormat) {
        int nChannels = audioFormat.getChannels();
        AudioFormat.Encoding encoding = audioFormat.getEncoding();
        int nSampleSize = audioFormat.getSampleSizeInBits();
        int nFormat = 0;
        if (nSampleSize == 8) {
            if (!encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                throw new IllegalArgumentException("encoding must be PCM_UNSIGNED for 8 bit data");
            }
            nFormat |= Esd.ESD_BITS8;
        } else if (nSampleSize == 16) {
            if (!encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
                throw new IllegalArgumentException("encoding must be PCM_SIGNED for 16 bit data");
            }
            nFormat |= Esd.ESD_BITS16;
        } else {
            throw new IllegalArgumentException("only 8 bit and 16 bit samples are supported");
        }
        if (nChannels == 1) {
            nFormat |= Esd.ESD_MONO;
        } else if (nChannels == 2) {
            nFormat |= Esd.ESD_STEREO;
        } else {
            throw new IllegalArgumentException("only mono and stereo are supported");
        }
        return nFormat;
    }
