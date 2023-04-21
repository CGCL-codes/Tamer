    public static String toMD5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buf = md.digest(data);
            return toHexString(buf);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
