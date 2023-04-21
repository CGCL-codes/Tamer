    public static byte[] sha(byte[] input) {
        MessageDigest sha = SHA.get();
        sha.reset();
        sha.update(input);
        return sha.digest();
    }
