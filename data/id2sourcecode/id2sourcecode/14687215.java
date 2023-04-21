    public String encryptedPassword(String aPassword) {
        String encryptedPassword = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            encryptedPassword = hexToString(digest.digest(aPassword.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.toString());
        }
        return encryptedPassword;
    }
