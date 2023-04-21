    public FloatSampleBuffer(byte[] buffer, int offset, int byteCount, AudioFormat format) {
        this(format.getChannels(), byteCount / (format.getSampleSizeInBits() / 8 * format.getChannels()), format.getSampleRate());
        initFromByteArray(buffer, offset, byteCount, format);
    }
