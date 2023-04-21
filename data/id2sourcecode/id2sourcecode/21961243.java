    private static byte[] buildValid128BitKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5").digest(key.getBytes(CHARACTOR_CODE));
    }
