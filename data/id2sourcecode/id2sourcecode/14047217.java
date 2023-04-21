    private void populateCache2() throws Exception {
        pair2 = kpg.generateKeyPair();
        privateKeyName2 = ContentName.fromNative("/test/priv2");
        cache2 = new SecureKeyCache();
        pubIdentifier2 = new PublisherPublicKeyDigest(pair2.getPublic()).digest();
        cache2.addPrivateKey(privateKeyName2, pubIdentifier2, pair2.getPrivate());
        myPair2 = kpg.generateKeyPair();
        myPubIdentifier2 = new PublisherPublicKeyDigest(myPair2.getPublic()).digest();
        cache2.addMyPrivateKey(myPubIdentifier2, myPair2.getPrivate());
        key2 = WrappedKey.generateNonceKey();
        keyName2 = ContentName.fromNative("/test/key2");
        keyIdentifier2 = SecureKeyCache.getKeyIdentifier(key2);
        cache2.addKey(keyName2, key2);
    }
