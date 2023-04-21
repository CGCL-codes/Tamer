    public boolean verify(byte[] digest, byte[] data) throws OTRCryptException {
        sha.update(data);
        byte[] trueDigest = sha.digest();
        return MessageDigest.isEqual(digest, trueDigest);
    }
