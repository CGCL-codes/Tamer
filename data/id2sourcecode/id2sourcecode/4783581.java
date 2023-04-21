    public static String mdSimple(byte[] array, int length) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        digest.update(array, 0, array.length);
        byte[] md5 = digest.digest();
        String res = byteToChars(md5);
        return res.substring(0, length);
    }
