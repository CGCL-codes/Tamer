    public byte[] encode(byte[] buffer) {
        return this.md.digest(buffer);
    }
