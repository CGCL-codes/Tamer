    @Test
    public void testValidation() throws Exception {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.yourict.net", 8080));
        NetworkConfig networkConfig = new NetworkConfig("proxy.yourict.net", 8080);
        URL url = new URL("https://idp.int.belgium.be");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(proxy);
        connection.connect();
        Certificate[] serverCertificates = connection.getServerCertificates();
        List<X509Certificate> certificateChain = new LinkedList<X509Certificate>();
        for (Certificate certificate : serverCertificates) {
            X509Certificate x509Cert = (X509Certificate) certificate;
            certificateChain.add(x509Cert);
            LOG.debug("certificate: " + x509Cert);
        }
        if (true) {
            return;
        }
        CertificatePathBuilder certificatePathBuilder = new CertificatePathBuilder();
        certificateChain = certificatePathBuilder.buildPath(certificateChain);
        MemoryCertificateRepository certificateRepository = new MemoryCertificateRepository();
        certificateRepository.addTrustPoint(certificateChain.get(certificateChain.size() - 1));
        TrustValidator trustValidator = new TrustValidator(certificateRepository);
        trustValidator.setAlgorithmPolicy(new AlgorithmPolicy() {

            @Override
            public void checkSignatureAlgorithm(String signatureAlgorithm) throws SignatureException {
            }
        });
        TrustValidatorDecorator trustValidatorDecorator = new TrustValidatorDecorator(networkConfig);
        trustValidatorDecorator.addDefaultTrustLinkerConfig(trustValidator);
        trustValidator.isTrusted(certificateChain);
    }
