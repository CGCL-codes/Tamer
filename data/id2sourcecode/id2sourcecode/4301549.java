    public static String hashPassword(String l_password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest((l_password + "ndi93HUF83ydnhYjt7$%^57hf").getBytes());
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception ex) {
            throw new RuntimeException("Could not get the MD5 hash algorithm.");
        }
    }
