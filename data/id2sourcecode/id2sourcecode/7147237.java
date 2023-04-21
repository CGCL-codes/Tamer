    public static String toMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buf = md.digest(str.getBytes());
            return toHexString(buf);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
