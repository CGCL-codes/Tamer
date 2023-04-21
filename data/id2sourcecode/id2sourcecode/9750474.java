    public static String getMD5HexString(String clear) throws NoSuchAlgorithmException {
        byte[] enc = MessageDigest.getInstance("MD5").digest(clear.getBytes());
        System.out.println(new String(enc));
        return encodeHexStr(enc, 0, enc.length);
    }
