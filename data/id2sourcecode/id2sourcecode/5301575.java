    public void testDigitalSignature_1() throws Exception {
        MessageDigest md5 = null;
        MessageDigest sha = null;
        md5 = MessageDigest.getInstance("MD5");
        sha = MessageDigest.getInstance("SHA-1");
        DigitalSignature ds_sign = new DigitalSignature(CipherSuite.KeyExchange_RSA_EXPORT);
        DigitalSignature ds_verify = new DigitalSignature(CipherSuite.KeyExchange_RSA_EXPORT);
        ds_sign.init(key);
        byte[] pivateKeyEncoding = key.getEncoded();
        ds_sign.update(pivateKeyEncoding);
        byte[] hash = ds_sign.sign();
        byte[] md5_hash = new byte[16];
        byte[] sha_hash = new byte[20];
        sha.update(pivateKeyEncoding);
        md5.update(pivateKeyEncoding);
        sha.digest(sha_hash, 0, sha_hash.length);
        md5.digest(md5_hash, 0, md5_hash.length);
        ds_verify.init(cert);
        ds_verify.setMD5(md5_hash);
        ds_verify.setSHA(sha_hash);
        assertTrue(ds_verify.verifySignature(hash));
    }
