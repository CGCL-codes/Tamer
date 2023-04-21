    @Override
    public String toString() {
        return String.format("FIFOByteBuffer(size=%d, bufferLength=%d, readOffset=%d, writeOffset=%d)", buffer.length, bufferLength, bufferReadOffset, bufferWriteOffset);
    }
