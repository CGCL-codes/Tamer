    public int getByteArrayBufferSize(AudioFormat format) {
        if (!format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            throw new IllegalArgumentException("FloatSampleBuffer: only PCM samples are possible.");
        }
        int bytesPerSample = format.getSampleSizeInBits() / 8;
        int bytesPerFrame = bytesPerSample * format.getChannels();
        return bytesPerFrame * getSampleCount();
    }
