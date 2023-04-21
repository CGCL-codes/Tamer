    public static int getFormatCode(AudioFormat format) {
        AudioFormat.Encoding encoding = format.getEncoding();
        int nSampleSize = format.getSampleSizeInBits();
        boolean bigEndian = format.isBigEndian();
        boolean frameSizeOK = (format.getFrameSize() == AudioSystem.NOT_SPECIFIED || format.getChannels() != AudioSystem.NOT_SPECIFIED || format.getFrameSize() == nSampleSize / 8 * format.getChannels());
        if (encoding.equals(AudioFormat.Encoding.ULAW) && nSampleSize == 8 && frameSizeOK) {
            return SND_FORMAT_MULAW_8;
        } else if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED) && frameSizeOK) {
            if (nSampleSize == 8) {
                return SND_FORMAT_LINEAR_8;
            } else if (nSampleSize == 16 && bigEndian) {
                return SND_FORMAT_LINEAR_16;
            } else if (nSampleSize == 24 && bigEndian) {
                return SND_FORMAT_LINEAR_24;
            } else if (nSampleSize == 32 && bigEndian) {
                return SND_FORMAT_LINEAR_32;
            }
        } else if (encoding.equals(AudioFormat.Encoding.ALAW) && nSampleSize == 8 && frameSizeOK) {
            return SND_FORMAT_ALAW_8;
        }
        return SND_FORMAT_UNSPECIFIED;
    }
