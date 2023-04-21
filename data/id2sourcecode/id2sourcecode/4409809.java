    public byte[] getByts(byte[] data) {
        digest.update(data);
        return digest.digest();
    }
