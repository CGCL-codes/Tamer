    public static byte[] sha1(byte[] data) {
        byte[] buf = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("sha1");
            buf = md5.digest(data);
        } catch (Exception e) {
            buf = null;
            e.printStackTrace(System.err);
        }
        return buf;
    }
