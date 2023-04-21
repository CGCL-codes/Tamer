    private static byte[] digest(String input, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            return messageDigest.digest(input.getBytes());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }
