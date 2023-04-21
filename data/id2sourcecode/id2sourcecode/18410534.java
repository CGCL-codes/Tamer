    private static String encode(MessageDigest digest, String salt, String toEncode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Assert.hasText(toEncode);
        digest.reset();
        return toHex(digest.digest((hasText(salt) ? toEncode + "{" + salt + "}" : toEncode).getBytes("UTF-8")));
    }
