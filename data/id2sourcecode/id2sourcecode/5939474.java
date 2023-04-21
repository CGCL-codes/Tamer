    static void isFullySpecifiedAudioFormat(AudioFormat format) {
        if (!format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED) && !format.getEncoding().equals(AudioFormat.Encoding.ULAW) && !format.getEncoding().equals(AudioFormat.Encoding.ALAW)) {
            return;
        }
        if (format.getFrameRate() <= 0) {
            throw new IllegalArgumentException("invalid frame rate: " + ((format.getFrameRate() == -1) ? "NOT_SPECIFIED" : String.valueOf(format.getFrameRate())));
        }
        if (format.getSampleRate() <= 0) {
            throw new IllegalArgumentException("invalid sample rate: " + ((format.getSampleRate() == -1) ? "NOT_SPECIFIED" : String.valueOf(format.getSampleRate())));
        }
        if (format.getSampleSizeInBits() <= 0) {
            throw new IllegalArgumentException("invalid sample size in bits: " + ((format.getSampleSizeInBits() == -1) ? "NOT_SPECIFIED" : String.valueOf(format.getSampleSizeInBits())));
        }
        if (format.getFrameSize() <= 0) {
            throw new IllegalArgumentException("invalid frame size: " + ((format.getFrameSize() == -1) ? "NOT_SPECIFIED" : String.valueOf(format.getFrameSize())));
        }
        if (format.getChannels() <= 0) {
            throw new IllegalArgumentException("invalid number of channels: " + ((format.getChannels() == -1) ? "NOT_SPECIFIED" : String.valueOf(format.getChannels())));
        }
    }
