    public static byte[] toMD5(String s) {
        try {
            return MessageDigest.getInstance("MD5").digest(getBytes(s, ISO_8859_1));
        } catch (NoSuchAlgorithmException err) {
            throw new RuntimeException(err);
        }
    }
