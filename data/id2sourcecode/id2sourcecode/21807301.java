    public boolean verify(byte[] digest, byte[] data, int offset, int length) throws OTRCryptException {
        sha.update(data, offset, length);
        byte[] trueDigest = sha.digest();
        return MessageDigest.isEqual(digest, trueDigest);
    }
