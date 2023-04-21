    public KeyPair generateKeysFromPassword(String password, byte[] salt, EncryptionParameters params) throws NoSuchAlgorithmException, NoSuchPaddingException {
        MessageDigest m;
        byte[] pwdData;
        byte[] md5Data;
        SecureRandom pseudoRandom;
        KeyPairGenerator keyGen;
        KeyPair keyPair;
        Cipher cipher = Cipher.getInstance(params.getAsymmetricCipher());
        int keysize = params.getAsymmetricKeySize();
        String algorithm = cipher.getAlgorithm();
        try {
            pwdData = password.getBytes(AdminService.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        m = MessageDigest.getInstance("MD5");
        m.update(xor(pwdData, salt));
        md5Data = m.digest();
        pseudoRandom = SecureRandom.getInstance("SHA1PRNG");
        pseudoRandom.setSeed(md5Data);
        keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keysize, pseudoRandom);
        keyPair = keyGen.generateKeyPair();
        return keyPair;
    }
