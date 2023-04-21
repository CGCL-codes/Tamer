    public static byte[] generateDerivedKey(String password, byte[] salt, int iteration) throws WSSecurityException {
        if (iteration == 0) {
            iteration = DEFAULT_ITERATION;
        }
        byte[] pwBytes = password.getBytes();
        byte[] pwSalt = new byte[salt.length + pwBytes.length];
        System.arraycopy(pwBytes, 0, pwSalt, 0, pwBytes.length);
        System.arraycopy(salt, 0, pwSalt, pwBytes.length, salt.length);
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new WSSecurityException(0, "noSHA1availabe");
        }
        sha.reset();
        byte[] K = sha.digest(pwSalt);
        for (int i = 2; i <= iteration; i++) {
            sha.reset();
            K = sha.digest(K);
        }
        return K;
    }
