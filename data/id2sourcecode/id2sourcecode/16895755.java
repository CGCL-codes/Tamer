    public static byte[] md5(Object obj) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            return md5.digest(obj.toString().getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
