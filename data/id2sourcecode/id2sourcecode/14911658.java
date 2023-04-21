    public static String MD5Encrypt(String origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = ValueUtil.toHexString(md.digest(origin.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            Assert.fail(ex);
        }
        return resultString;
    }
