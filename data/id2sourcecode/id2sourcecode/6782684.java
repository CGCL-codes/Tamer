    @Override
    public PrivateKey getSigningKey(PublisherPublicKeyDigest publisher) {
        if (Log.isLoggable(Log.FAC_KEYS, Level.FINER)) Log.finer(Log.FAC_KEYS, "getSigningKey: retrieving key: " + publisher);
        if (null == publisher) return null;
        return _privateKeyCache.getPrivateKey(publisher.digest());
    }
