    private void createLocalCA() {
        System.out.println("Creating the local CA");
        try {
            KeyPair newKeyPair;
            newKeyPair = CertUtil.generateKeyPair(props._defAsymAlgo, props._defKeyLength);
            CertsKeysProxiesFileReaderWriter.writePrivateKeyToPEMFile(newKeyPair.getPrivate(), props._defCAKey);
            System.out.println("UNENCRYPTED CA's private key stored in the file " + props._defCAKey);
            X509Certificate newCert = signCertificate(defLocalCAX509Name, defLocalCAX509Name, newKeyPair.getPublic(), newKeyPair, props._defMainCertLifetime);
            System.out.println("The local CA's self-cert subject is " + newCert.getSubjectDN().getName());
            CertsKeysProxiesFileReaderWriter.writeX509CertToPEMFile(newCert, props._defCACert);
            System.out.println("Local CA's self-signed proxy stored in file " + props._defCACert);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
