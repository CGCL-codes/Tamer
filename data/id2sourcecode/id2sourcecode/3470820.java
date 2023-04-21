    public static String SHA1_Base64(byte[] source) {
        MessageDigest digest = messageDigestThreadLocal.get();
        digest.reset();
        digest.update(source);
        byte[] key = digest.digest();
        BASE64Encoder en = new BASE64Encoder();
        String res = en.encode(key);
        return res;
    }
