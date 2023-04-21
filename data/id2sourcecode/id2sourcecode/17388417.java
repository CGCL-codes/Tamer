    public static final byte[] chapEncrypt(final byte chapIdentifier, final byte[] plaintextPassword, byte[] chapChallenge) {
        byte[] chapPassword = plaintextPassword;
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            md5MessageDigest.reset();
            md5MessageDigest.update(chapIdentifier);
            md5MessageDigest.update(plaintextPassword);
            chapPassword = md5MessageDigest.digest(chapChallenge);
        } catch (NoSuchAlgorithmException nsaex) {
            throw new RuntimeException("Could not access MD5 algorithm, fatal error");
        }
        return chapPassword;
    }
