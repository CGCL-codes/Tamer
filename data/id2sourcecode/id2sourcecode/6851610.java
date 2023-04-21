    public static String encryptByMD5(String plaintext) {
        MD5.reset();
        MD5.update(plaintext.getBytes());
        byte[] digest = MD5.digest();
        return byteToHexString(digest);
    }
