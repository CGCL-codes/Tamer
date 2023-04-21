    public static byte[] createChecksum(String algorithm, InputStream is) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(IOUtil.read(is));
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
