    public void setupTest() throws Exception {
        if (setupDone) {
            return;
        }
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(dummyWrappedKey);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        wrappingKeyPair = kpg.generateKeyPair();
        wrappedKeyPair = kpg.generateKeyPair();
        wrappingKeyID = PublisherID.generatePublicKeyDigest(wrappingKeyPair.getPublic());
        wrappingKeyName = VersioningProfile.addVersion(ContentName.fromNative("/parc/Users/briggs/KEY"));
        ElGamalParameterSpec egp = new ElGamalParameterSpec(new BigInteger(1, pbytes), new BigInteger(1, gbytes));
        KeyPairGenerator ekpg = KeyPairGenerator.getInstance("ElGamal");
        ekpg.initialize(egp);
        wrappingEGKeyPair = ekpg.generateKeyPair();
        wrappedEGKeyPair = ekpg.generateKeyPair();
        byte[] key = new byte[16];
        sr.nextBytes(key);
        wrappingAESKey = new SecretKeySpec(key, "AES");
        sr.nextBytes(key);
        wrappedAESKey = new SecretKeySpec(key, "AES");
        ContentName nodeName = testHelper.getClassNamespace().append(ContentName.fromNative("/test/content/File1.txt"));
        storedKeyName = GroupAccessControlProfile.nodeKeyName(nodeName);
        setupDone = true;
        Log.info("Initialized keys for WrappedKeyTest");
    }
