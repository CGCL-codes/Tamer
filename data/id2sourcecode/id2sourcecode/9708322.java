    public static String digest62(final byte[] bytes) throws NoSuchAlgorithmException {
        return toBase62String(new BigInteger(MessageDigest.getInstance("SHA-512").digest(Lifecycle.getSaltedHash(bytes))));
    }
