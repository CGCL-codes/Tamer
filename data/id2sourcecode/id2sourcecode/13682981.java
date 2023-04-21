    public String randomValue() {
        try {
            Random generator = new Random();
            String randomNum = new Integer(generator.nextInt(100000)).toString();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
            return hexEncode(result).toString().substring(0, 11);
        } catch (NoSuchAlgorithmException ex) {
            return "notrandom";
        }
    }
