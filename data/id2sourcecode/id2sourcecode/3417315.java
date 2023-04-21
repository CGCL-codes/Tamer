    public static String getMd5(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(bytes);
        return "" + new BigInteger(1, m.digest()).toString(16);
    }
