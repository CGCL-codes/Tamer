    public ChallengeImpl(String password) {
        challengeType = Challenge.VERIFY_PASS;
        keyType = "MD5";
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
        } catch (NoSuchAlgorithmException f) {
            return;
        }
        byte[] pass = password.getBytes();
        decodedChallenge = getRandomBytes();
        byte[] buffer = new byte[pass.length + decodedChallenge.length];
        for (int i = 0; i < decodedChallenge.length; i++) buffer[i] = decodedChallenge[i];
        for (int i = 0; i < pass.length; i++) buffer[i + decodedChallenge.length] = pass[i];
        algorithm.update(buffer);
        encodedChallenge = algorithm.digest();
    }
