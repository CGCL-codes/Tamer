    public static String hashPassword(final String password, final byte[] salt, final String algorithm) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(salt);
        final byte[] hashedPassword = digest.digest(password.getBytes());
        return Base64.encodeBytes(hashedPassword, false);
    }
