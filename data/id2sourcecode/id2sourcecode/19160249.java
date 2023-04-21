    public static String hashMD5(String senha) {
        String hash = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bigInteger = new BigInteger(1, md.digest(senha.getBytes()));
        hash = bigInteger.toString();
        return hash;
    }
