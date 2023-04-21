    static String getMd5Digest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return pad(number.toString(16), 32, '0');
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
