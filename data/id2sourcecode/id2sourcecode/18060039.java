    protected TimeStampRequest getTimeStampRequest(final byte[] imprint) {
        Digester digester = new BasicDigester(DigestType.valueOf(DigestType.SHA1.getAlgorithm()));
        byte[] digest = digester.digest(imprint);
        TimeStampRequestGenerator generator = new TimeStampRequestGenerator();
        generator.setCertReq(true);
        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        TimeStampRequest request = generator.generate(BouncyCastleTimeStampClient.RSA_OID, digest, nonce);
        return request;
    }
