    public static byte[] SHA1(byte[] source) {
        MessageDigest digest = messageDigestThreadLocal.get();
        digest.reset();
        digest.update(source);
        byte[] key = digest.digest();
        return key;
    }
