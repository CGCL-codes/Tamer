    public static KeyStore getClientKeyStore(String host) throws Exception {
        if (kstCache.containsKey(host)) {
            return kstCache.get(host);
        }
        KeyStore ks = KeyStore.getInstance("JKS");
        File fakeSslFile = getFakeSSLCertFile(host);
        InputStream is = fakeSslFile.exists() ? new FileInputStream(fakeSslFile) : null;
        ks.load(is, null == is ? null : KS_PASS.toCharArray());
        if (null == is) {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(Long.toString(System.currentTimeMillis()).getBytes());
            keyGen.initialize(2048, random);
            final KeyPair keypair = keyGen.generateKeyPair();
            X509Certificate cert = createClientCert(host, keypair.getPublic());
            ks.setKeyEntry(CLIENT_CERT_ALIAS, keypair.getPrivate(), KS_PASS.toCharArray(), new Certificate[] { cert, caCert });
            FileOutputStream fos = new FileOutputStream(fakeSslFile);
            ks.store(fos, KS_PASS.toCharArray());
        } else {
            is.close();
        }
        kstCache.put(host, ks);
        return ks;
    }
