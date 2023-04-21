    private String hexHashSHA1(String phrase) {
        byte[] phraseBytes = phrase.getBytes();
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA");
            hasher.reset();
            hasher.update(phraseBytes);
            BigInteger digest = new BigInteger(1, hasher.digest());
            return digest.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
