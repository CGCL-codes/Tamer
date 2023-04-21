    public static void main(String[] args) throws Throwable {
        X509V3CertificateGenerator gen = new X509V3CertificateGenerator();
        gen.setIssuerDN(new X509Name("CN=OpenGroove Root CA"));
        gen.setNotBefore(new Date());
        gen.setNotAfter(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(20 * 365, TimeUnit.DAYS)));
        gen.setSerialNumber(new BigInteger("" + System.currentTimeMillis()));
        gen.setSignatureAlgorithm("SHA512withRSA");
        gen.setSubjectDN(new X509Name("CN=OpenGroove Root CA"));
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(3072);
        System.out.println("generating keys...");
        KeyPair keys = keygen.generateKeyPair();
        System.out.println("keys generated.");
        RSAPublicKey pub = (RSAPublicKey) keys.getPublic();
        RSAPrivateKey prv = (RSAPrivateKey) keys.getPrivate();
        gen.setPublicKey(pub);
        X509Certificate cert = gen.generate(prv);
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(null, "pass".toCharArray());
        keystore.setKeyEntry("key", prv, "pass".toCharArray(), new Certificate[] { cert });
        keystore.store(new FileOutputStream("C:\\opengroove-ca.jks"), "pass".toCharArray());
    }
