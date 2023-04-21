    public void copy(int channel, int sourceIndex, int destIndex, int length) {
        float[] data = getChannel(channel);
        int bufferCount = getSampleCount();
        if (sourceIndex + length > bufferCount || destIndex + length > bufferCount || sourceIndex < 0 || destIndex < 0 || length < 0) {
            throw new IndexOutOfBoundsException("parameters exceed buffer size");
        }
        System.arraycopy(data, sourceIndex, data, destIndex, length);
    }
