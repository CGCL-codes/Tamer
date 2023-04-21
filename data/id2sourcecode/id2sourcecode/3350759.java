    public static String doSHA1Hex(String s) throws Throwable {
        byte abyte1[];
        MessageDigest messagedigest = MessageDigest.getInstance("SHA");
        byte abyte0[] = s.getBytes();
        messagedigest.update(abyte0, 0, s.length());
        abyte1 = messagedigest.digest();
        return toHexString(abyte1);
    }
