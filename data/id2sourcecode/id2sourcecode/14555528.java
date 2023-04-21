    @Override
    public String sh1withRSA(String texto) {
        String sh1 = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1withRSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(texto.getBytes()));
        sh1 = hash.toString(16);
        return sh1;
    }
