    public static final byte[] createHash(String value) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA");
        byte[] hashBytes = digest.digest((value).getBytes("UTF-8"));
        return hashBytes;
    }
