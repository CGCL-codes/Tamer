    public SecureKeyCache(KeyManager keyManagerToLoadFrom) {
        PrivateKey[] pks = keyManagerToLoadFrom.getSigningKeys();
        for (PrivateKey pk : pks) {
            PublisherPublicKeyDigest ppkd = keyManagerToLoadFrom.getPublisherKeyID(pk);
            Log.info("KeyCache: loading signing key {0}", ppkd);
            addMyPrivateKey(ppkd.digest(), pk);
        }
    }
