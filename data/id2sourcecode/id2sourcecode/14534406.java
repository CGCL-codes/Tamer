    private static byte[] generateSaltFromPassword(String password, CryptnosApplication theApp) throws Exception {
        byte[] salt = password.getBytes(CryptnosApplication.TEXT_ENCODING_UTF8);
        MessageDigest hasher = MessageDigest.getInstance("SHA-512");
        for (int i = 0; i < SALT_ITERATION_COUNT; i++) {
            salt = hasher.digest(salt);
        }
        return salt;
    }
