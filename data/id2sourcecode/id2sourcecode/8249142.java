    public byte[] generateHash(String algorithm, byte[] source) throws Exception {
        MessageDigest _md = MessageDigest.getInstance(algorithm);
        return _md.digest(source);
    }
