    private static KeyStore getDefaultKeyStore() throws HTTPSException {
        try {
            if (defaultKeyStore == null) {
                defaultKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                try {
                    defaultKeyStore.load(new FileInputStream("APJP.jks"), "APJP".toCharArray());
                } catch (Exception e) {
                    defaultKeyStore.load(null, "APJP".toCharArray());
                    KeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
                    keyPairGenerator.initialize(1024);
                    KeyPair keyPair = keyPairGenerator.generateKeyPair();
                    X509Certificate x509CertificateAuthority = new X509Certificate();
                    Name name = new Name();
                    name.addRDN(new ObjectID("2.5.4.3"), "APJP");
                    name.addRDN(new ObjectID("2.5.4.10"), "APJP");
                    name.addRDN(new ObjectID("2.5.4.11"), "APJP");
                    x509CertificateAuthority.setSubjectDN(name);
                    x509CertificateAuthority.setIssuerDN(name);
                    x509CertificateAuthority.setValidNotBefore(new Date(new Date().getTime() - 1 * (1000L * 60 * 60 * 24 * 365)));
                    x509CertificateAuthority.setValidNotAfter(new Date(new Date().getTime() + 10 * (1000L * 60 * 60 * 24 * 365)));
                    x509CertificateAuthority.setSerialNumber(BigInteger.valueOf(new Date().getTime()));
                    x509CertificateAuthority.setPublicKey(keyPair.getPublic());
                    x509CertificateAuthority.sign(new AlgorithmID(new ObjectID("1.2.840.113549.1.1.5")), keyPair.getPrivate());
                    x509CertificateAuthority.writeTo(new FileOutputStream("APJP.pem"));
                    X509Certificate[] x509CertificateArray = new X509Certificate[1];
                    x509CertificateArray[0] = x509CertificateAuthority;
                    defaultKeyStore.setCertificateEntry("APJP", x509CertificateAuthority);
                    defaultKeyStore.setKeyEntry("APJP", keyPair.getPrivate(), "APJP".toCharArray(), x509CertificateArray);
                    defaultKeyStore.store(new FileOutputStream("APJP.jks"), "APJP".toCharArray());
                }
            }
            return defaultKeyStore;
        } catch (Exception e) {
            logger.log(2, "HTTPS/GET_DEFAULT_KEY_STORE: EXCEPTION", e);
            throw new HTTPSException("HTTPS/GET_DEFAULT_KEY_STORE", e);
        }
    }
