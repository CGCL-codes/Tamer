    private String digestPasswd(String orig) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        return new BASE64Encoder().encode(digest.digest(orig.getBytes()));
    }
