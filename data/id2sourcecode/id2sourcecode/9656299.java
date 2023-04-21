    public static byte[] hash(byte[] input) {
        sha256.reset();
        return sha256.digest(input);
    }
