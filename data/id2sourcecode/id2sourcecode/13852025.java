    public void testAesSHA256Password() throws Exception {
        String password = "zhengrenqi";
        MessageDigest SHA_256 = MessageDigest.getInstance("SHA-256");
        byte[] key = SHA_256.digest(password.getBytes());
        MessageDigest MD5 = MessageDigest.getInstance("MD5");
        byte[] iv = MD5.digest(password.getBytes());
        byte[] text = "This is zhengrenqi's text.".getBytes();
        runCipherSymmetric("AES/CBC/PKCS5Padding", key, "AES", iv, text);
    }
