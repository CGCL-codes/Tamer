    protected static AudioFormat getSignOrEndianChangedFormat(AudioFormat format) {
        boolean isSigned = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
        boolean isUnsigned = format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
        if (format.getSampleSizeInBits() > 8 && isSigned) {
            return new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), !format.isBigEndian());
        } else if (format.getSampleSizeInBits() == 8 && (isSigned || isUnsigned)) {
            return new AudioFormat(isSigned ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), format.isBigEndian());
        }
        return null;
    }
