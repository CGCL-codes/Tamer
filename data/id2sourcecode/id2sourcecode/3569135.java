    private byte[] calculateHashGen(String algorithm, String encoding, int hashingIterations, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes(encoding));
        for (int i = 0; i < hashingIterations; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }
