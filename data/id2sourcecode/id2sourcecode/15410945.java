    public byte[] calculateHash(ByteBuffer buffer) {
        sha1.reset();
        return sha1.digest(buffer);
    }
