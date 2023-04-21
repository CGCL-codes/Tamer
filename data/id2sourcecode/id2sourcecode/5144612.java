    private static String calculateDigest(MessageDigest digest, byte[] bytes) {
        return new String(Base64.encode(digest.digest(bytes)));
    }
