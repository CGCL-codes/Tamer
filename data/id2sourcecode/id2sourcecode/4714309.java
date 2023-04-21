    public static byte[] hashString(String s) {
        if (s == null) return null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(s.getBytes());
        } catch (java.security.NoSuchAlgorithmException nse) {
            return null;
        }
    }
