    public static void generateNewCASignedKeystore(String ksLocation, char[] ksPass, Provider provider, String caKSLocation, String caKSAlias, char[] caKSPass, Provider caProvider, String caType, String caSigAlg, Vector<KeyDefinition> keys) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        Certificate[] caChain;
        Certificate[] chain;
        KeyDefinition key;
        PrivateKey caKey;
        SignKey signer;
        File newKSFile;
        String alias;
        Iterator it;
        X509Certificate signedCert = null;
        X509Certificate trustedCert = null;
        X509Certificate cert = null;
        FileOutputStream fos = null;
        KeyStore newKS = null;
        KeyStore caKS = null;
        KeyPair pair = null;
        try {
            if (provider == null) {
                newKS = KeyStore.getInstance("JKS", provider);
            } else {
                newKS = KeyStore.getInstance("JKS");
            }
        } catch (KeyStoreException kse) {
            if (provider == null) {
                throw new KeyStoreException("The requested keystore type is not available from the " + "default provider!");
            } else {
                throw new KeyStoreException("The requested keystore type is not available from the " + "chosen provider!");
            }
        } catch (IllegalArgumentException iae) {
            if (provider == null) {
                throw new IllegalArgumentException("The default provider is empty! Choose another one!");
            } else {
                throw new KeyStoreException("The chosen provider is empty! Choose another one!");
            }
        }
        newKS.load(null, ksPass);
        try {
            caKS = openJKS(caKSLocation, caKSPass);
            if (!caKS.isKeyEntry(caKSAlias)) {
                throw new Exception("The certification alias doesn't contain a key entry!");
            }
            caChain = caKS.getCertificateChain(caKSAlias);
            if (caChain == null) {
                throw new Exception("The certificate chain of the certification authority " + "alias cannot be null!");
            }
            trustedCert = (X509Certificate) (caChain[caChain.length - 1]);
            caKey = (PrivateKey) caKS.getKey(caKSAlias, caKSPass);
            if (!caKey.getAlgorithm().equals(caType)) {
                throw new Exception("The private key of the CA certificate is not of type " + caType + ". It is of type " + caKey.getAlgorithm() + ". Please choose as type " + caKey.getAlgorithm() + " and choose a suitable signature!");
            }
            newKS.setCertificateEntry(caKSAlias, trustedCert);
        } catch (Exception e) {
            throw new KeyStoreException("There occurred the following exception while trying " + "to extract the trusted certificate with alias: \"" + caKSAlias + "\" certification keystore: \"" + caKSLocation + "\" and import it into the keystore \"" + ksLocation + "\":\n" + e.getClass().getName() + ": " + e.getMessage());
        }
        it = keys.iterator();
        while (it.hasNext()) {
            key = (KeyDefinition) it.next();
            try {
                pair = generateKeyPair(key.getType(), key.getKeysize(), provider);
                cert = generateCertificate(key.getDNameObject(), key.getDNameObject(), key.getValidity(), caSigAlg, pair.getPublic());
                chain = new Certificate[1];
                chain[0] = cert;
                alias = key.getAlias();
            } catch (Exception e) {
                throw new KeyStoreException("There occurred the following problem while trying to " + "generate the self-signed certificate of the entry with " + "parameters:\n Alias: " + key.getAlias() + "\nKey ID: " + key.getName() + ":\n" + e.getClass().getName() + ": " + e.getMessage());
            }
            signer = new SignKey();
            try {
                signedCert = signer.sign(trustedCert, caKey, cert, caSigAlg, key.getUsageArray(), null);
            } catch (Exception e) {
                throw new KeyStoreException("There occurred the following problem while trying to " + "sign the certificate of the entry with parameters: " + "\n Alias: " + key.getAlias() + "\nKey ID: " + key.getName() + ":\n" + e.getClass().getName() + ": " + e.getMessage());
            }
            chain = new Certificate[2];
            chain[0] = signedCert;
            chain[1] = trustedCert;
            try {
                newKS.setKeyEntry(alias, pair.getPrivate(), ksPass, chain);
            } catch (Exception e) {
                throw new KeyStoreException("There occurred the following problem while trying to " + "import the entry with parameters: " + "\n Alias: " + key.getAlias() + "\nKey ID: " + key.getName() + ":\n" + e.getClass().getName() + ": " + e.getMessage());
            }
        }
        try {
            newKSFile = new File(ksLocation);
            newKSFile.createNewFile();
            fos = new FileOutputStream(newKSFile);
            newKS.store(fos, ksPass);
            fos.close();
        } catch (Exception e) {
            throw new KeyStoreException("There occurred the following problem while trying to " + "save the generated keystore in the file: " + ksLocation + ":\n" + e.getClass().getName() + ": " + e.getMessage());
        }
    }
