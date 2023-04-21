    public static byte[] getHash(String algorithm, byte[] input) throws GeneralSecurityException {
        MessageDigest sha = MessageDigest.getInstance(algorithm);
        sha.reset();
        sha.update(input, 0, input.length);
        return sha.digest();
    }
