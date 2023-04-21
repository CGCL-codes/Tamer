    public static String getHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (password == null) {
            password = "";
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        return new String(digest.digest(password.getBytes("UTF-8")));
    }
