    static boolean isFullySpecifiedPCMFormat(AudioFormat format) {
        if (!format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            return false;
        }
        if ((format.getFrameRate() <= 0) || (format.getSampleRate() <= 0) || (format.getSampleSizeInBits() <= 0) || (format.getFrameSize() <= 0) || (format.getChannels() <= 0)) {
            return false;
        }
        return true;
    }
