    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(512);
        pair = kpg.generateKeyPair();
        privateKeyName = ContentName.fromNative("/test/priv");
        cache = new SecureKeyCache();
        pubIdentifier = new PublisherPublicKeyDigest(pair.getPublic()).digest();
        cache.addPrivateKey(privateKeyName, pubIdentifier, pair.getPrivate());
        myPair = kpg.generateKeyPair();
        myPubIdentifier = new PublisherPublicKeyDigest(myPair.getPublic()).digest();
        cache.addMyPrivateKey(myPubIdentifier, myPair.getPrivate());
        key = WrappedKey.generateNonceKey();
        keyName = ContentName.fromNative("/test/key");
        keyIdentifier = SecureKeyCache.getKeyIdentifier(key);
        cache.addKey(keyName, key);
        File f = new File(file);
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(cache);
        out.close();
    }
