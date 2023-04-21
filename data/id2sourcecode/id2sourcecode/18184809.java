    public static String encrypt(String text, String algorithm, String charset) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] bytes = messageDigest.digest(text.getBytes(charset));
        return encryptBASE64(bytes);
    }
