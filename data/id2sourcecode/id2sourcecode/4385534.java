    public static synchronized String getHash(byte[] input) {
        digest.update(input);
        byte[] hash = digest.digest();
        return hashToString(hash);
    }
