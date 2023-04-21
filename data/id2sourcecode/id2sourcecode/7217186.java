    public byte[] hash(byte[] data, int offset, int length) throws OTRCryptException {
        sha.update(data, offset, length);
        return sha.digest();
    }
