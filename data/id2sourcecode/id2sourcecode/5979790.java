    public static String md5(String texto) {
        if (texto == null) return null;
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(texto.getBytes()));
        sen = hash.toString(16);
        return sen;
    }
