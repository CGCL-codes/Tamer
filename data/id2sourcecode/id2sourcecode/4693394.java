    public static UID generate() {
        try {
            if (prng == null) initialize();
            String randomNum = new Integer(prng.nextInt()).toString();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            return new UID(sha.digest(randomNum.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
        }
        return null;
    }
