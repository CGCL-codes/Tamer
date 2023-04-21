    public int put(short appId, byte[] key, byte[] value, int ttl, byte[] secret) {
        if (secret == null) {
            if (largePut(appId, key, value, ttl, null) < 0) return -1;
        } else {
            md.update(secret);
            byte[] token = new byte[20];
            random.nextBytes(token);
            md.update(token);
            byte[] secretKey = md.digest();
            byte[] hashSecretKey = md.digest(secretKey);
            if (putNS(NS_HASH_STORAGE, hashSecretKey, token, ttl, hashSecretKey) != bamboo_stat.BAMBOO_OK) {
                return -1;
            }
            if (largePut(appId, key, value, ttl, hashSecretKey) < 0) {
                return -1;
            }
        }
        return 0;
    }
