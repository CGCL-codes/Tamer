    private void populateCache1() throws Exception {
        pair1 = kpg.generateKeyPair();
        privateKeyName1 = ContentName.fromNative("/test/priv1");
        cache1 = new SecureKeyCache();
        pubIdentifier1 = new PublisherPublicKeyDigest(pair1.getPublic()).digest();
        cache1.addPrivateKey(privateKeyName1, pubIdentifier1, pair1.getPrivate());
        myPair1 = kpg.generateKeyPair();
        myPubIdentifier1 = new PublisherPublicKeyDigest(myPair1.getPublic()).digest();
        cache1.addMyPrivateKey(myPubIdentifier1, myPair1.getPrivate());
        key1 = WrappedKey.generateNonceKey();
        keyName1 = ContentName.fromNative("/test/key1");
        keyIdentifier1 = SecureKeyCache.getKeyIdentifier(key1);
        cache1.addKey(keyName1, key1);
    }
