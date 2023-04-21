    public static final void main(String[] argv) {
        try {
            KeyGenerator gen = KeyGenerator.getInstance("AES");
            gen.init(new SecureRandom("656F43C3E8F28DE517EF20BA3742424F".getBytes("ISO8859-1")));
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, gen.generateKey());
            IOUtils.write(cipher.doFinal("1234567890ABCDEF".getBytes("ISO8859-1")), new FileOutputStream("C:\\aes\\target.aes"));
            cipher.init(Cipher.DECRYPT_MODE, gen.generateKey());
            IOUtils.write(cipher.doFinal("1234567890ABCDEF".getBytes("ISO8859-1")), new FileOutputStream("C:\\aes\\target.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
