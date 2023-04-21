    public static int getFormatCode(AudioFormat format) {
        AudioFormat.Encoding encoding = format.getEncoding();
        int nSampleSize = format.getSampleSizeInBits();
        boolean bigEndian = format.isBigEndian();
        boolean frameSizeOK = format.getFrameSize() == AudioSystem.NOT_SPECIFIED || format.getChannels() != AudioSystem.NOT_SPECIFIED || format.getFrameSize() == nSampleSize / 8 * format.getChannels();
        if ((encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) && ((bigEndian && nSampleSize >= 16 && nSampleSize <= 32) || (nSampleSize == 8)) && frameSizeOK) {
            return AIFF_COMM_PCM;
        } else if (encoding.equals(AudioFormat.Encoding.ULAW) && nSampleSize == 8 && frameSizeOK) {
            return AIFF_COMM_ULAW;
        } else if (encoding.equals(new AudioFormat.Encoding("IMA_ADPCM")) && nSampleSize == 4) {
            return AIFF_COMM_IMA_ADPCM;
        } else {
            return AIFF_COMM_UNSPECIFIED;
        }
    }
