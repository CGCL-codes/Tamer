    public static String getMD5HexString(String clear) throws NoSuchAlgorithmException {
        byte[] enc = MessageDigest.getInstance("MD5").digest(clear.getBytes());
        return encodeHexStr(enc, 0, enc.length);
    }
