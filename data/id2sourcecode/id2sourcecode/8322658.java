    private static byte[] getKey(final String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return MessageDigest.getInstance("MD5").digest(secret.getBytes());
    }
