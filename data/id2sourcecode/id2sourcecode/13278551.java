    public String cifrar(String claveEnPlano) throws Exception {
        MessageDigest md;
        byte[] textBytes = claveEnPlano.getBytes("UTF-8");
        md = MessageDigest.getInstance("MD5");
        md.update(textBytes, 0, claveEnPlano.length());
        byte[] outputBytes = md.digest();
        BigInteger hash = new BigInteger(1, outputBytes);
        String claveCifrada = hash.toString(16);
        return claveCifrada;
    }
