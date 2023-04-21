    private void createSERVENTCertAndKey() {
        System.out.println("Creating the SERVENT key and cert.");
        try {
            KeyPair newKeyPair;
            newKeyPair = CertUtil.generateKeyPair(props._defAsymAlgo, props._defKeyLength);
            CertsKeysProxiesFileReaderWriter.writePrivateKeyToPEMFile(newKeyPair.getPrivate(), props._defSERVENTKey);
            System.out.println("UNENCRYPTED SERVENT's private" + " key stored in the file " + props._defSERVENTKey);
            X509Certificate newCert = signCertificate(defSERVENTX509Name, defSERVENTX509Name, newKeyPair.getPublic(), newKeyPair, props._defMainCertLifetime);
            System.out.println("SERVENT's self-cert subject is " + newCert.getSubjectDN().getName());
            CertsKeysProxiesFileReaderWriter.writeX509CertToPEMFile(newCert, props._defSERVENTCert);
            System.out.println("SERVENT's self-signed proxy stored in file " + props._defSERVENTCert);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
