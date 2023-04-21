    private byte[] generateBytes() throws Exception {
        if (digest == null) {
            digest = MessageDigest.getInstance("SHA");
            long seed = 0;
            for (int i = 0; i < password.length; i++) seed = (seed * 37) + password[i];
            random = new Random(seed);
        }
        random.nextBytes(toDigest);
        System.arraycopy(password, 0, toDigest, 0, password.length);
        return digest.digest(toDigest);
    }
