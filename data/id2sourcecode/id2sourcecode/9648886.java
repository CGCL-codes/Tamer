    public static byte[] md5(byte[] by) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            return messagedigest.digest(by);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[] {};
    }
