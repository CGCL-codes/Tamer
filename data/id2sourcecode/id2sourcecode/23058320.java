    public static byte[] hash(final byte[] bytes) throws GeneralSecurityException {
        final MessageDigest digest = MessageDigest.getInstance(MD5);
        return digest.digest(bytes);
    }
