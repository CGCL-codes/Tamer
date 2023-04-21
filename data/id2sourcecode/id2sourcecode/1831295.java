    public static byte[] hash(String algorithm, byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
