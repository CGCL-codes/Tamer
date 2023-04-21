    private void createUserIDBE() {
        System.out.println("Creating the local user IDBE");
        try {
            KeyPair newKeyPair;
            newKeyPair = CertUtil.generateKeyPair(props._defAsymAlgo, props._defKeyLength);
            CertsKeysProxiesFileReaderWriter.writePrivateKeyToPEMFile(newKeyPair.getPrivate(), props._defUserKey);
            System.out.println("UNENCRYPTED user's private key " + "stored in the file " + props._defUserKey);
            X509Certificate newCert = signCertificate(defUserX509Name, defUserX509Name, newKeyPair.getPublic(), newKeyPair, props._defMainCertLifetime);
            System.out.println("The local user's self-cert subject is " + newCert.getSubjectDN().getName());
            CertsKeysProxiesFileReaderWriter.writeX509CertToPEMFile(newCert, props._defUserIDBE);
            System.out.println("User's self-signed proxy stored in file " + props._defUserIDBE);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
