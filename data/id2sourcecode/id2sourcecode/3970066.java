    public static final byte[] getMessageDigest(byte[] b, String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm).digest(b);
        } catch (NoSuchAlgorithmException e) {
            throw new CustomRuntimeException(e);
        }
    }
