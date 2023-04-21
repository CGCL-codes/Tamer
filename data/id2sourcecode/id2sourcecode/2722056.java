    public static String GenerateId() {
        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = new Integer(prng.nextInt()).toString();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
            spectrumID = hexEncode(result);
            spectrumID = "sid_" + spectrumID;
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
        }
        return spectrumID;
    }
