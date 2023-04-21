    public static String generateID() {
        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = String.valueOf(prng.nextInt());
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
            String id = hexEncode(result);
            return id;
        } catch (NoSuchAlgorithmException e) {
            throw new CException(e);
        }
    }
