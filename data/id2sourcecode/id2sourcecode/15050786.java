    public static String getDigest(String str, String privateKey) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte buf[] = str.getBytes();
        byte _key[] = privateKey.getBytes();
        md.update(buf, 0, buf.length);
        return toHexString(md.digest(_key));
    }
