    public static TimeStampToken createTimeStampToken(byte[] data, PrivateKey privateKey, List<X509Certificate> certificateChain) throws Exception {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(TSPAlgorithms.SHA1);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = md.digest(data);
        CollectionCertStoreParameters collectionCertStoreParameters = new CollectionCertStoreParameters(certificateChain);
        CertStore certStore = CertStore.getInstance("Collection", collectionCertStoreParameters);
        TimeStampRequestGenerator requestGen = new TimeStampRequestGenerator();
        requestGen.setCertReq(true);
        TimeStampRequest request = requestGen.generate(TSPAlgorithms.SHA1, digest);
        TimeStampTokenGenerator tstGen = new TimeStampTokenGenerator(privateKey, certificateChain.get(0), TSPAlgorithms.SHA1, "1.2");
        tstGen.setCertificatesAndCRLs(certStore);
        return tstGen.generate(request, BigInteger.ONE, new Date(), "BC");
    }
