    public static void main(String[] args) throws Throwable {
        KeyStore signstore = KeyStore.getInstance("JKS");
        signstore.load(new FileInputStream("selfsigned.jks"), pass);
        KeyStore certstore = KeyStore.getInstance("JKS");
        certstore.load(null, pass);
        PrivateKey signkey = (PrivateKey) signstore.getKey("key", pass);
        X509Certificate signcert = (X509Certificate) signstore.getCertificate("key");
        X509V3CertificateGenerator gen = new X509V3CertificateGenerator();
        gen.setIssuerDN(signcert.getSubjectX500Principal());
        gen.setNotBefore(new Date());
        gen.setNotAfter(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(180, TimeUnit.DAYS)));
        gen.setSerialNumber(new BigInteger("" + System.currentTimeMillis()));
        gen.setSignatureAlgorithm("SHA512withRSA");
        gen.setSubjectDN(new X509Name("CN=example.com"));
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(3072);
        System.out.println("generating keys...");
        KeyPair keys = keygen.generateKeyPair();
        System.out.println("keys generated.");
        RSAPublicKey pub = (RSAPublicKey) keys.getPublic();
        RSAPrivateKey prv = (RSAPrivateKey) keys.getPrivate();
        gen.setPublicKey(pub);
        X509Certificate cert = gen.generate(signkey);
        certstore.setKeyEntry("key", prv, pass, new Certificate[] { cert, signcert });
        certstore.store(new FileOutputStream("example.jks"), pass);
    }
