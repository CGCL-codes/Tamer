    public void createImpersonationIDBEProxy(File idbeFullPathFile, int lifetime, String newProxyDir) {
        BouncyCastleCertProcessingFactory proxyFacto = BouncyCastleCertProcessingFactory.getDefault();
        try {
            X509Certificate userIDBE = CertsKeysProxiesFileReaderWriter.readCertFromPEMFile(idbeFullPathFile.getAbsolutePath());
            PrivateKey userIDBEPrivateKey = CertsKeysProxiesFileReaderWriter.readPrivateKeyFromPEMFile(supposedAssociatedPrivateKeyFullPathFile(idbeFullPathFile.getAbsolutePath()));
            KeyPair newKeyPair;
            newKeyPair = CertUtil.generateKeyPair(props._defAsymAlgo, props._defKeyLength);
            byte[] proxyRequest = proxyFacto.createCertificateRequest(userIDBE, newKeyPair);
            InputStream proxyRequestStream = new ByteArrayInputStream(proxyRequest);
            X509Certificate newProxy = proxyFacto.createCertificate(proxyRequestStream, userIDBE, userIDBEPrivateKey, lifetime, GSIConstants.GSI_3_IMPERSONATION_PROXY);
            CertsKeysProxiesFileReaderWriter.writeX509CertToPEMFile(newProxy, newProxyDir + supposedAssociatedProxyFileName(idbeFullPathFile.getName()));
            CertsKeysProxiesFileReaderWriter.writePrivateKeyToPEMFile(newKeyPair.getPrivate(), supposedAssociatedPrivateKeyFullPathFile(newProxyDir + supposedAssociatedProxyFileName(idbeFullPathFile.getName())));
            System.out.println("New impersonation Proxy IDBE created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
