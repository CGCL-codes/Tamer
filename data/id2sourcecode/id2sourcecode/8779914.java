    public static byte[] createSHADigest(byte[] data) {
        try {
            MessageDigest md = createSHADigester();
            return md.digest(data);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create SHA-256 digest.", e);
        }
    }
