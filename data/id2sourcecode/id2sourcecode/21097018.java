    public static short getFormatCode(AudioFormat format) {
        AudioFormat.Encoding encoding = format.getEncoding();
        int nSampleSize = format.getSampleSizeInBits();
        boolean littleEndian = !format.isBigEndian();
        boolean frameSizeOK = format.getFrameSize() == AudioSystem.NOT_SPECIFIED || format.getChannels() != AudioSystem.NOT_SPECIFIED || format.getFrameSize() == nSampleSize / 8 * format.getChannels();
        if (nSampleSize == 8 && frameSizeOK && (encoding.equals(AudioFormat.Encoding.PCM_SIGNED) || encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED))) {
            return WAVE_FORMAT_PCM;
        } else if (nSampleSize > 8 && frameSizeOK && littleEndian && encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            return WAVE_FORMAT_PCM;
        } else if (encoding.equals(AudioFormat.Encoding.ULAW) && (nSampleSize == AudioSystem.NOT_SPECIFIED || nSampleSize == 8) && frameSizeOK) {
            return WAVE_FORMAT_ULAW;
        } else if (encoding.equals(AudioFormat.Encoding.ALAW) && (nSampleSize == AudioSystem.NOT_SPECIFIED || nSampleSize == 8) && frameSizeOK) {
            return WAVE_FORMAT_ALAW;
        } else if (encoding.equals(new AudioFormat.Encoding("IMA_ADPCM")) && nSampleSize == 4) {
            return WAVE_FORMAT_IMA_ADPCM;
        } else if (encoding.equals(GSM0610)) {
            return WAVE_FORMAT_GSM610;
        }
        return WAVE_FORMAT_UNSPECIFIED;
    }
