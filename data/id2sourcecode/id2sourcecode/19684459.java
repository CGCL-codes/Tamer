    public static void main(String[] args) throws Exception {
        java.security.Security.addProvider(new cryptix.jce.provider.CryptixCrypto());
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA", "CryptixCrypto");
        keygen.initialize(keyLength, new SecureRandom());
        KeyPair pair = keygen.generateKeyPair();
        writeToFile("key.pub", pair.getPublic());
        Properties keyStore = new Properties();
        keyStore.put("key.prv", pair.getPrivate());
        KeyStore.save(keyStore);
        System.out.println("key.pub and the server keystore file are generated");
    }
