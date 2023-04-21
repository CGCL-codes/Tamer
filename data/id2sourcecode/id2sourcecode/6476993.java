    public static String generatePasswordHash(String password) {
        String hash = null;
        password = HASH_SALT + password;
        try {
            MessageDigest sha = MessageDigest.getInstance(HASH_METHOD);
            byte[] hashedBytes = sha.digest(password.getBytes());
            hash = convertToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            logger.w("Error generating password hash for [" + password + "]", e);
        }
        return hash;
    }
