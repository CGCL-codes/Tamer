    @Override
    public String md5(String texto) {
        String md5 = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(texto.getBytes()));
        md5 = hash.toString(16);
        return md5;
    }
