    private static synchronized byte[] getPrivateKey() {
        if (privateKey != null) {
            return privateKey;
        }
        MD5DigestWrapper md5 = new MD5DigestWrapper();
        md5.update(createTimestamp());
        privateKey = md5.digest();
        return privateKey;
    }
