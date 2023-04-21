    private static synchronized byte[] createNonce() {
        MD5DigestWrapper md5 = new MD5DigestWrapper();
        md5.update(createTimestamp());
        md5.update(column);
        md5.update(getPrivateKey());
        return md5.digest();
    }
