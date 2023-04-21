    public byte[] generateHash(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        byte[] hashValue;
        MessageDigest hash = MessageDigest.getInstance(algorithm);
        hash.update(data);
        hashValue = hash.digest();
        return hashValue;
    }
