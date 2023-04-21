    private void createCA() throws InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, KeyStoreException, CertificateException, IOException {
        File toDoODir = new File(SWTConstants.TODO_O_HOME);
        if (!toDoODir.exists()) {
            toDoODir.mkdirs();
        } else if (!toDoODir.isDirectory()) {
            toDoODir.delete();
            toDoODir.mkdirs();
        }
        keyStore = KeyStore.getInstance("JKS");
        if (new File(SWTConstants.KEYSTORE_FILE).exists()) {
            keyStore.load(new FileInputStream(SWTConstants.KEYSTORE_FILE), "quaerite et invenietis".toCharArray());
        } else {
            keyStore.load(null);
        }
        trustStore = KeyStore.getInstance("JKS");
        if (new File(SWTConstants.TRUSTSTORE_FILE).exists()) {
            trustStore.load(new FileInputStream(SWTConstants.TRUSTSTORE_FILE), "quaerite et invenietis".toCharArray());
        } else {
            trustStore.load(null);
        }
        String systemId = getSystemID();
        if (!keyStore.containsAlias(systemId) || !trustStore.containsAlias(systemId)) {
            JDKKeyPairGenerator.RSA keyGen = new JDKKeyPairGenerator.RSA();
            keyGen.initialize(SWTConstants.KEY_SIZE);
            KeyPair keyPair = keyGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            X500Principal dName = new X500Principal("CN=" + systemId);
            Calendar cal = Calendar.getInstance();
            cal.set(2010, 1, 1);
            Date startDate = cal.getTime();
            cal.set(2020, 12, 31);
            Date endDate = cal.getTime();
            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            certGen.setSerialNumber(new BigInteger("1"));
            certGen.setIssuerDN(dName);
            certGen.setSubjectDN(dName);
            certGen.setNotBefore(startDate);
            certGen.setNotAfter(endDate);
            certGen.setPublicKey(publicKey);
            certGen.setSignatureAlgorithm("SHA1withRSA");
            cert = certGen.generate(privateKey, "BC");
            keyStore.setKeyEntry(systemId, privateKey, "quaerite et invenietis".toCharArray(), new Certificate[] { cert });
            keyStore.store(new FileOutputStream(SWTConstants.KEYSTORE_FILE), "quaerite et invenietis".toCharArray());
            trustStore.setCertificateEntry(systemId, cert);
            trustStore.store(new FileOutputStream(SWTConstants.TRUSTSTORE_FILE), "quaerite et invenietis".toCharArray());
        }
    }
