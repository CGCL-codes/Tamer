    private String cypherPassword(String password) throws NoSuchAlgorithmException {
        return new String(MessageDigest.getInstance(getPasswordEncryptionAlgorithm()).digest(password.getBytes()));
    }
