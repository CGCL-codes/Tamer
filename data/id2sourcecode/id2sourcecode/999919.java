    public static KeyStore getClientKeyStore(String host) throws Exception {
        if (kstCache.containsKey(host)) {
            return kstCache.get(host);
        }
        KeyStore ks = KeyStore.getInstance("JKS");
        File kst_file = new File(AppData.GetFakeSSLCertHome(), host + ".kst");
        if (kst_file.exists()) {
            ks.load(new FileInputStream(kst_file), KS_PASS.toCharArray());
            kstCache.put(host, ks);
            return ks;
        } else {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(Long.toString(System.currentTimeMillis()).getBytes());
            keyGen.initialize(2048, random);
            final KeyPair keypair = keyGen.generateKeyPair();
            X509Certificate cert = createClientCert(host, keypair.getPublic());
            ks.load(null, null);
            ks.setKeyEntry(CLIENT_CERT_ALIAS, keypair.getPrivate(), KS_PASS.toCharArray(), new Certificate[] { cert, caCert });
            ks.store(new FileOutputStream(kst_file), KS_PASS.toCharArray());
            kstCache.put(host, ks);
            return ks;
        }
    }
