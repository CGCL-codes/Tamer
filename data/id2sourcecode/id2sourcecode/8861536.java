    public static String hashPassword(final String plainTextPassword, String hashingAlgorithm, String charsetName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String result = null;
        MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm);
        result = new String(digest.digest(plainTextPassword.getBytes(charsetName)), charsetName);
        return result;
    }
