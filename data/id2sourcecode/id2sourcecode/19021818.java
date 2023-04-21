    private String calculateMD5(byte[] b) throws java.security.NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(b, 0, b.length);
        return new BigInteger(1, m.digest()).toString(16);
    }
