    public byte[] hash(byte[] data) throws OTRCryptException {
        sha.update(data);
        return sha.digest();
    }
