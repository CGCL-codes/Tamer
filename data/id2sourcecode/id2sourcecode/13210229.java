    public int remove(short appId, byte[] key, byte[] secret) {
        ArrayList rootValues = new ArrayList();
        if (getNS(appId, getDigest(key), Integer.MAX_VALUE, rootValues) != bamboo_stat.BAMBOO_OK) return -1;
        Iterator it = rootValues.iterator();
        while (it.hasNext()) {
            Triple value = (Triple) it.next();
            int ttl = ((Integer) value.third).intValue();
            if (secret == null) {
                if (removeNS(appId, getDigest(key), value.first, ttl, null) < 0) return -1;
            } else {
                byte[] hashSecretKey = value.second;
                ArrayList tokens = new ArrayList();
                if (getNS(NS_HASH_STORAGE, hashSecretKey, Integer.MAX_VALUE, tokens) != bamboo_stat.BAMBOO_OK) {
                    return -1;
                }
                if (tokens.size() == 0) {
                    return -1;
                }
                byte[] token = ((Triple) tokens.get(0)).first;
                md.update(secret);
                md.update(token);
                byte[] secretKey = md.digest();
                if (!Arrays.equals(md.digest(secretKey), hashSecretKey)) {
                    logger.error("something is wrong... hash of a secret key is incorrect!");
                    return -1;
                }
                if (removeNS(appId, getDigest(key), value.first, ttl, secretKey) < 0) {
                    return -1;
                }
                if (removeNS(NS_HASH_STORAGE, hashSecretKey, token, ttl, secretKey) < 0) {
                    return -1;
                }
            }
        }
        return 0;
    }
