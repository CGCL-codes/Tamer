    public static String HashMD5(String data) {
        try {
            MessageDigest msgDig = MessageDigest.getInstance("MD5");
            byte[] messageDigest = msgDig.digest(data.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hash = number.toString(16);
            while (hash.length() < 32) hash = "0" + hash;
            return hash;
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
