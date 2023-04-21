    private static String encodePassword(String rawPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte buf[] = rawPassword.getBytes("UTF-8");
        buf = MessageDigest.getInstance("SHA").digest(buf);
        return Base64.encodeBytes(buf);
    }
