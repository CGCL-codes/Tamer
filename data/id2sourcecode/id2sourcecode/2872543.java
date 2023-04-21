    public static byte[] getHash(int iterationNumber, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNumber; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }
