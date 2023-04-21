    public static String doMD5Hex(String s) throws Throwable {
        byte abyte1[];
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        byte abyte0[] = s.getBytes();
        messagedigest.update(abyte0, 0, s.length());
        abyte1 = messagedigest.digest();
        return toHexString(abyte1);
    }
