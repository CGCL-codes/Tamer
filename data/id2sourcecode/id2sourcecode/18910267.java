    public synchronized void merge(SecureKeyCache cache) {
        for (PrivateKey pkey : cache._myKeyMap.values()) {
            byte[] identifier = cache.getPublicKeyIdentifier(pkey).digest();
            if (!this._myKeyMap.containsKey(identifier)) {
                this.addMyPrivateKey(identifier, pkey);
            }
        }
        for (PrivateKey pkey : cache._privateKeyMap.values()) {
            byte[] identifier = cache.getPublicKeyIdentifier(pkey).digest();
            ContentName name = cache.getContentName(identifier);
            if (!this._privateKeyMap.containsKey(identifier)) {
                this.addPrivateKey(name, identifier, pkey);
            } else {
                if (this.getContentName(identifier) == null) {
                    _nameKeyMap.put(name, identifier);
                }
            }
        }
        for (Key key : cache._keyMap.values()) {
            byte[] identifier = getKeyIdentifier(key);
            ContentName name = cache.getContentName(identifier);
            if (!this.containsKey(identifier)) {
                this.addKey(name, key);
            } else {
                if (this.getContentName(identifier) == null) {
                    _nameKeyMap.put(name, identifier);
                }
            }
        }
    }
