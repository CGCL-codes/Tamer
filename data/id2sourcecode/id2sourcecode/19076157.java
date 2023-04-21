    public static String getPasswordHash(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bSalt = base64ToByte(salt);
        MessageDigest digest = MessageDigest.getInstance(HASH_FUNCTION);
        digest.reset();
        digest.update(bSalt);
        byte[] input = digest.digest(password.getBytes(CHARSET));
        for (int i = 0; i < ITERATION_NUMBER; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        String passwordHash = byteToBase64(input);
        return passwordHash;
    }
