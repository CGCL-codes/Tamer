    @Test
    public void bouncycastleHashAlgos() throws Exception {
        byte[] message = "hello world".getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("RIPEMD160", new BouncyCastleProvider());
        byte[] digest = messageDigest.digest(message);
        LOG.debug("RIPEMD160 size: " + digest.length);
        messageDigest = MessageDigest.getInstance("RIPEMD128", new BouncyCastleProvider());
        digest = messageDigest.digest(message);
        LOG.debug("RIPEMD128 size: " + digest.length);
        messageDigest = MessageDigest.getInstance("RIPEMD256", new BouncyCastleProvider());
        digest = messageDigest.digest(message);
        LOG.debug("RIPEMD256 size: " + digest.length);
        messageDigest = MessageDigest.getInstance("RIPEMD320", new BouncyCastleProvider());
        digest = messageDigest.digest(message);
        LOG.debug("RIPEMD320 size: " + digest.length);
        messageDigest = MessageDigest.getInstance("SHA-224", new BouncyCastleProvider());
        digest = messageDigest.digest(message);
        LOG.debug("SHA-224 size: " + digest.length);
    }
