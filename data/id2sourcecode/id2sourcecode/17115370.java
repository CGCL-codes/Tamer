    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            for (int i = 0; i < KEY_COUNT; ++i) {
                pairs[i] = kpg.generateKeyPair();
                publishers[i] = new PublisherPublicKeyDigest(pairs[i].getPublic());
                keyLocs[i] = new KeyLocator(new ContentName(keyprefix, publishers[i].digest()));
            }
        } catch (Exception e) {
            System.out.println("Exception in test setup: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
