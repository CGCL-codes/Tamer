    public static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return StringUtilities.toHex(createMessageDigest().digest(password.getBytes("utf-8")));
    }
