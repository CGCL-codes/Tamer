    public final byte[] getHashValue(int numberOfInteractions, String password, byte[] salt) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(DIGEST_TYPE);
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes(CHARSET));
        for (int i = 0; i < numberOfInteractions; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }
