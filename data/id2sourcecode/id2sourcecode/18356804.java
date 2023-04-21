    private void createTempDefaultUserIDBEProxy() {
        System.out.println("Creating the temporary proxy of the user's " + "default IDBE");
        BouncyCastleCertProcessingFactory proxyFacto = BouncyCastleCertProcessingFactory.getDefault();
        try {
            X509Certificate userIDBEMainCert = CertsKeysProxiesFileReaderWriter.readCertFromPEMFile(props._defUserIDBE);
            PrivateKey userIDBEPrivateKey = CertsKeysProxiesFileReaderWriter.readPrivateKeyFromPEMFile(props._defUserKey);
            KeyPair newKeyPair;
            newKeyPair = CertUtil.generateKeyPair(props._defAsymAlgo, props._defKeyLength);
            byte[] proxyRequest = proxyFacto.createCertificateRequest(userIDBEMainCert, newKeyPair);
            InputStream proxyRequestStream = new ByteArrayInputStream(proxyRequest);
            X509Certificate newProxy = proxyFacto.createCertificate(proxyRequestStream, userIDBEMainCert, userIDBEPrivateKey, (((int) props._defProxyLifetime) / 1000), GSIConstants.GSI_3_IMPERSONATION_PROXY);
            CertsKeysProxiesFileReaderWriter.writeX509CertToPEMFile(newProxy, props._defUserProxyIDBE);
            CertsKeysProxiesFileReaderWriter.writePrivateKeyToPEMFile(newKeyPair.getPrivate(), props._defUserProxyIDBEKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
