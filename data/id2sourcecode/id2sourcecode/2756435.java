    private int availableImpl() {
        if (writeIndex >= readIndex) return writeIndex - readIndex; else return writeIndex + buf.length - readIndex;
    }
