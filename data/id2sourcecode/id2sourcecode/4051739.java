    private int getBytesInBuffer() {
        if (writeIndex < readIndex) {
            return (buffer.capacity() - readIndex) + writeIndex;
        }
        return writeIndex - readIndex;
    }
