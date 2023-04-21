    public static String MD5Hash(String to_hash) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        md5.digest(to_hash.getBytes());
        BigInteger hash = new BigInteger(1, md5.digest(to_hash.getBytes()));
        return hash.toString(16);
    }
