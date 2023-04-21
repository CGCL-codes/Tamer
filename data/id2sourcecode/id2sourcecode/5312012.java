    @Test
    public void connect() throws Exception {
        logger.info(transportKeyStore.toString());
        KeyStore trustStore = SecurityUtils.openTrustStore(TUSTSTORE_PASSWORD.toCharArray());
        logger.info("Armaz�m de chaves seguras (cacerts) aberto: {}", trustStore.getProvider());
        X509TrustManager trustManager = openTrustManager(trustStore);
        logger.info("Gerenciador de chaves aberto: {}", trustManager.toString());
        connectSSL(trustManager, instaladorServicosEstaduais.getHostHomologacao());
        X509Certificate[] chain = ((TrustManagerDecorator) trustManager).chain;
        if (chain == null) {
            logger.warn("N�o pode obter cadeia de certifica��o do servidor.");
            return;
        }
        logger.info("Certificados requeridos: ");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            logger.info("Subject[{}] {}", i, cert.getSubjectDN());
            logger.info("Issuer[{}]  {}", i, cert.getIssuerDN());
            sha1.update(cert.getEncoded());
            logger.info("sha[{}]     {}", i, toHexString(sha1.digest()));
            md5.update(cert.getEncoded());
            logger.info("md5[{}]     {}", i, toHexString(md5.digest()));
        }
    }
