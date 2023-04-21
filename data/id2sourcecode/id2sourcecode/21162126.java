    public static byte[] sha(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA");
        return md.digest(s.getBytes("utf-8"));
    }
