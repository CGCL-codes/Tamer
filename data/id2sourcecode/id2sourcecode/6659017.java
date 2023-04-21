    public static byte[] generateKeyID(byte[] key) {
        return CCNDigestHelper.digest(key);
    }
