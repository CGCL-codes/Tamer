    @Override
    public String encrypt(String value) throws GeneralSecurityException {
        if (!keyFile.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(KEY_SIZE);
            SecretKey sk = keyGen.generateKey();
            FileWriter fw = null;
            try {
                fw = new FileWriter(keyFile);
                fw.write(byteArrayToHexString(sk.getEncoded()));
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        SecretKeySpec sks = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }
