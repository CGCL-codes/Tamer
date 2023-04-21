    public static void generateKey(File privateKeyFile, File publicKeyFile, Algorithm algorithm) throws IOException, GeneralSecurityException {
        KeyPair key = KeyPairGenerator.getInstance(algorithm.algorithm()).generateKeyPair();
        Base64Utils.serializeTo(key.getPrivate(), privateKeyFile);
        Base64Utils.serializeTo(key.getPublic(), publicKeyFile);
    }
