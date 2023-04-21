    @Override
    public PrivateKey getDefaultSigningKey() {
        return _privateKeyCache.getPrivateKey(getDefaultKeyID().digest());
    }
