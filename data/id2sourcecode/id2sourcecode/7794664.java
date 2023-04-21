    public static String createSalt(int length) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[40];
        RANDOM_GENERATOR.nextBytes(bytes);
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        byte[] digest = sha1.digest(bytes);
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest)).substring(0, length);
    }
