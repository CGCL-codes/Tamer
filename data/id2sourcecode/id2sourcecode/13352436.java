    public static byte[] createDigest(byte[] bytes, String digestType) throws Exception {
        MessageDigest complete = MessageDigest.getInstance(digestType);
        return complete.digest(bytes);
    }
