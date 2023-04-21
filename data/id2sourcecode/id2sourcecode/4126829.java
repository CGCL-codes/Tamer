    static byte[] getMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
