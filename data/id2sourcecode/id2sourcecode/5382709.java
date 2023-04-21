    private int getPayloadType() {
        if (audioFormat.getEncoding() == AudioFormat.Encoding.ULAW) {
            return 0;
        } else if (audioFormat.getEncoding() == AudioFormat.Encoding.ALAW) {
            return 8;
        } else if (audioFormat.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
            if (audioFormat.getSampleSizeInBits() == 16) {
                if (audioFormat.getChannels() == 2) {
                    return 10;
                } else if (audioFormat.getChannels() == 1) {
                    return 11;
                }
            }
        } else if (audioFormat.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED) {
            if (audioFormat.getSampleSizeInBits() == 8) {
                throw new RuntimeException("Dynamic payload type...");
            }
        } else {
            throw new RuntimeException("Unknown audio format. Cannot guess payload type");
        }
        return 1;
    }
