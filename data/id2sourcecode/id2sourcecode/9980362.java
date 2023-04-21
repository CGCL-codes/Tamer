    public static void main(String[] args) throws Exception {
        String password = "password";
        KeyStore keyStore = KeyStore.getInstance("JKS");
        File keyStoreFile = new File("C:/temp/try.jks");
        if (keyStoreFile.isFile()) {
            InputStream inputStream = new FileInputStream(keyStoreFile);
            keyStore.load(inputStream, password.toCharArray());
        } else {
            keyStore.load(null, password.toCharArray());
        }
        String publicKeyAlias1 = "lpublic";
        String privateKeyAlias1 = "lprivate";
        String password1 = "lpassword";
        Key publicKey = keyStore.getKey(publicKeyAlias1, password1.toCharArray());
        System.out.println(publicKey);
        Key privateKey = keyStore.getKey(privateKeyAlias1, password1.toCharArray());
        System.out.println(privateKey);
        if (publicKey == null) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            keyStore.setKeyEntry(privateKeyAlias1, keyPair.getPrivate(), password1.toCharArray(), null);
        }
    }
