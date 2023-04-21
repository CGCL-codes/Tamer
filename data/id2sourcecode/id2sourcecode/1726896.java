    public static byte[] calcSHA1(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(b);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
