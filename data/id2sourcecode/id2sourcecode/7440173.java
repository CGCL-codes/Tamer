    public void testEngineEncRandDecPubInfoFromFile() {
        UTMAStrongParameters utmaParameters = null;
        try {
            UTMAStrongParametersGenerator utmaStrongParametersGenerator = new UTMAStrongParametersGenerator();
            utmaParameters = utmaStrongParametersGenerator.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/rfid/utma/strong/utmas.params"));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
        utmaStrongKeyPairGenerator.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), utmaParameters));
        AsymmetricCipherKeyPair keyPair = utmaStrongKeyPairGenerator.generateKeyPair();
        try {
            AsymmetricBlockCipher strongEngine = new MultiBlockAsymmetricBlockCipher(new UTMAStrongEngine(), new PKCS7Padding());
            String message = "Hello World!!!";
            byte[] messageAsBytes = message.getBytes();
            strongEngine.init(true, keyPair.getPublic());
            byte[] cipherText = strongEngine.processBlock(messageAsBytes, 0, messageAsBytes.length);
            UTMAStrongRandomizer randomizer = new UTMAStrongRandomizer();
            randomizer.init(utmaParameters);
            for (int i = 0; i < 10; i++) {
                cipherText = randomizer.processBlock(cipherText, 0, cipherText.length);
            }
            strongEngine.init(false, keyPair.getPrivate());
            byte[] plainText = strongEngine.processBlock(cipherText, 0, cipherText.length);
            assertEquals(message, new String(plainText).trim());
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
