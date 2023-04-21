    public static String get(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        BigInteger hash = new BigInteger(1, md.digest(password.getBytes()));
        return hash.toString(16);
    }
