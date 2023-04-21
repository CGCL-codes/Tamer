    public static void generateNewSelfSignedKeystore(String ksLocation, char[] ksPass, Provider provider, Vector<KeyDefinition> keys) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, InvalidKeyException {
        Certificate[] chain;
        KeyDefinition key;
        SignKey signer;
        File newKSFile;
        String alias;
        Iterator it;
        FileOutputStream fos = null;
        X509Certificate cert = null;
        X509Certificate s_cert = null;
        KeyStore newKS = null;
        KeyPair pair = null;
        try {
            newKS = KeyStore.getInstance("JKS");
        } catch (KeyStoreException kse) {
            throw new KeyStoreException("The requested keystore type is not available from the " + "default provider!");
        } catch (IllegalArgumentException iae) {
            throw new KeyStoreException("The chosen provider is empty! Choose another one!");
        }
        newKS.load(null, ksPass);
        it = keys.iterator();
        while (it.hasNext()) {
            key = (KeyDefinition) it.next();
            try {
                pair = generateKeyPair(key.getType(), key.getKeysize(), provider);
                cert = generateCertificate(key.getDNameObject(), key.getDNameObject(), key.getValidity(), key.getSigAlg(), pair.getPublic());
                chain = new Certificate[1];
                chain[0] = cert;
            } catch (Exception e) {
                throw new KeyStoreException("There occurred the following problem while trying to " + "generate key and certificates for the entry with parameters:\n Alias: " + key.getAlias() + "\nKey ID: " + key.getName() + ":\n" + e.getClass().getName() + ": " + e.getMessage());
            }
            signer = new SignKey();
            try {
                s_cert = signer.sign(cert, pair.getPrivate(), cert, key.getSigAlg(), key.getUsageArray(), new Integer(0));
            } catch (Exception e) {
                throw new KeyStoreException("There occurred the following problem while trying to " + "sign the certificate of the entry with parameters: " + "\n Alias: " + key.getAlias() + "\nKey ID: " + key.getName() + ":\n" + e.getClass().getName() + ": " + e.getMessage());
            }
            chain = new Certificate[1];
            chain[0] = s_cert;
            alias = key.getAlias();
            newKS.setKeyEntry(alias, pair.getPrivate(), ksPass, chain);
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
