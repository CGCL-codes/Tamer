    public byte[] getHash(byte[] data) {
        byte[] hash = hc.digest(data);
        hc.reset();
        return hash;
    }
