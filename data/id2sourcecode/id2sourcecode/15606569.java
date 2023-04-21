    public static byte[] digest(String algorithm, byte[] ba) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return digest.digest(ba);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
