    public byte[] getHash() {
        if (!closed) {
            throw new IllegalStateException("The stream is not closed");
        }
        return dg.digest();
    }
