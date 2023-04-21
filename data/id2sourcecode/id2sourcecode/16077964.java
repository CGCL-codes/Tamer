    public static String generateMD5Hash(String source) {
        String result = "";
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            byte[] data = source.getBytes();
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1, m.digest());
            result = String.format("%1$032X", i);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
