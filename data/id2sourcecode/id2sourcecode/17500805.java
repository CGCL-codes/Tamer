    public static byte[] computeMd5(Object k) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
        md5.reset();
        md5.update(getKeyBytes(k));
        return md5.digest();
    }
