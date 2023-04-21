    public String encrypt(String text) {
        String encrypt = new String();
        try {
            ecipher.init(Cipher.ENCRYPT_MODE, KEY);
            encrypt = new BASE64Encoder().encode(ecipher.doFinal(text.getBytes()));
        } catch (Exception e) {
            Log.writeWarningLog(Coder.class, e.getMessage(), e);
        }
        return encrypt;
    }
