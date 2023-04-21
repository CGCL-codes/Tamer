    public static String digest16(final byte[] bytes) throws NoSuchAlgorithmException {
        return toHexString(MessageDigest.getInstance("SHA-512").digest(Lifecycle.getSaltedHash(bytes)));
    }
