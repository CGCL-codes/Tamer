    public static String encodePassword(final String password) {
        String encodedPassword = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            byte[] original = new byte[] {};
            if (password != null) {
                original = password.getBytes();
            }
            byte[] bytes = md5.digest(original);
            for (byte b : bytes) {
                encodedPassword = encodedPassword + DIGITS[(b >> 4) & 0x0F] + DIGITS[b & 0x0F];
            }
        } catch (NoSuchAlgorithmException ex) {
            encodedPassword = password;
        }
        return encodedPassword;
    }
