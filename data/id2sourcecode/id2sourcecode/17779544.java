    public static byte[] createChecksum(String algorithm, byte[] source) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(source);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
