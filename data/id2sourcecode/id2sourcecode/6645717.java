    public static byte[] ntlmHash(String password) throws Exception {
        byte[] unicodePassword = password.getBytes("UnicodeLittleUnmarked");
        MessageDigest md4 = MessageDigest.getInstance("MD4");
        return md4.digest(unicodePassword);
    }
