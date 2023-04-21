    public static byte[] md5(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(s.getBytes("utf-8"));
    }
