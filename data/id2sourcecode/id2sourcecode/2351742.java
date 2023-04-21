    static int getFormatType(AudioFormat format) {
        boolean signed = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
        if (!signed && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            throw new IllegalArgumentException("unsupported encoding: only PCM encoding supported.");
        }
        if (!signed && format.getSampleSizeInBits() != 8) {
            throw new IllegalArgumentException("unsupported encoding: only 8-bit can be unsigned");
        }
        checkSupportedSampleSize(format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize());
        int formatType = getFormatType(format.getSampleSizeInBits(), format.getFrameSize() / format.getChannels(), signed, format.isBigEndian());
        return formatType;
    }
