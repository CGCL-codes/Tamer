    public static byte[] md5(byte[] data) {
        byte[] md5buf = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5buf = md5.digest(data);
        } catch (Exception e) {
            md5buf = null;
            e.printStackTrace(System.err);
        }
        return md5buf;
    }
