    public void generateKey(String keyAlgorithm, String keySpec, String alias, char[] authCode) throws CryptoTokenOfflineException, IllegalArgumentException {
        if (keySpec == null) {
            throw new IllegalArgumentException("Missing keyspec parameter");
        }
        if (alias == null) {
            throw new IllegalArgumentException("Missing alias parameter");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("keyAlgorithm: " + keyAlgorithm + ", keySpec: " + keySpec + ", alias: " + alias);
        }
        try {
            final KeyStore keystore = getKeystore(keystoretype, keystorepath, authenticationCode);
            final Provider prov = keystore.getProvider();
            if (LOG.isDebugEnabled()) {
                LOG.debug("provider: " + prov);
            }
            final KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyAlgorithm, prov);
            if ("ECDSA".equals(keyAlgorithm)) {
                kpg.initialize(ECNamedCurveTable.getParameterSpec(keySpec));
            } else {
                kpg.initialize(Integer.valueOf(keySpec));
            }
            final String sigAlgName = "SHA1With" + keyAlgorithm;
            LOG.debug("generating...");
            final KeyPair keyPair = kpg.generateKeyPair();
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = getSelfCertificate("CN=" + alias + ", " + SUBJECT_DUMMY + ", C=SE", (long) 30 * 24 * 60 * 60 * 365, sigAlgName, keyPair);
            LOG.debug("Creating certificate with entry " + alias + '.');
            keystore.setKeyEntry(alias, keyPair.getPrivate(), authCode, chain);
            keystore.store(new FileOutputStream(new File(keystorepath)), authenticationCode);
        } catch (Exception ex) {
            LOG.error(ex, ex);
            throw new CryptoTokenOfflineException(ex);
        }
    }
