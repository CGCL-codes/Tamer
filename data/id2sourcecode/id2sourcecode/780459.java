    private KeyPair generateFileServerKeys() throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator fileKeysGen = KeyPairGenerator.getInstance("RSA", "BC");
            System.out.println("keypairgenerator: " + fileKeysGen.toString());
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            fileKeysGen.initialize(1024, random);
            KeyPair dsaFileKey = fileKeysGen.generateKeyPair();
            return dsaFileKey;
        } catch (NoSuchProviderException e) {
            System.out.println("No such provider available.");
            return null;
        }
    }
