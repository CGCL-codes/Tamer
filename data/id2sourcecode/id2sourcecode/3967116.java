    public static final byte[] chapEncrypt(final byte chapIdentifier, final byte[] plaintextPassword, byte[] chapChallenge) {
        byte[] chapPassword = plaintextPassword;
        MessageDigest md5 = getMd5();
        md5.update(chapIdentifier);
        md5.update(plaintextPassword);
        chapPassword = md5.digest(chapChallenge);
        return chapPassword;
    }
