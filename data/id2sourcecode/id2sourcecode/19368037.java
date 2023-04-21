    public void RunTest() {
        Thread t = new Thread(this);
        t.start();
        SecureRandom srand = new SecureRandom();
        ElGamalParametersGenerator generator = new ElGamalParametersGenerator();
        generator.init(768, 5, srand);
        ElGamalParameters ElGamalParms = generator.generateParameters();
        ElGamalKeyGenerationParameters genparms = new ElGamalKeyGenerationParameters(srand, ElGamalParms);
        ElGamalKeyPairGenerator keygen = new ElGamalKeyPairGenerator();
        keygen.init(genparms);
        AsymmetricCipherKeyPair keypair = keygen.generateKeyPair();
        PublicKey = (ElGamalPublicKeyParameters) keypair.getPublic();
        PrivateKey = (ElGamalPrivateKeyParameters) keypair.getPrivate();
        try {
            new OutgoingProcessor("127.0.0.1,6543", TransferFile, File.createTempFile("blah", ".dat"), new OutgoingCB(), PublicKey, srand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
