    private static byte[] decode(InputStream is, String pass) throws Exception {
        Cipher c = Cipher.getInstance("DES");
        Key k = new SecretKeySpec(pass.getBytes(), "DES");
        c.init(Cipher.DECRYPT_MODE, k);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CipherInputStream cis = new CipherInputStream(is, c);
        for (int b; (b = cis.read()) != -1; ) bos.write(b);
        cis.close();
        return bos.toByteArray();
    }
