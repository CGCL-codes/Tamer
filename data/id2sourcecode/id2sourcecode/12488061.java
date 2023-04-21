    @Before
    public void setUp() throws Exception {
        this.servletTester = new ServletTester();
        this.servletHolder = this.servletTester.addServlet(AppletServiceServlet.class, "/");
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = generateKeyPair();
        DateTime notBefore = new DateTime();
        DateTime notAfter = notBefore.plusMonths(1);
        this.certificate = generateSelfSignedCertificate(keyPair, "CN=localhost", notBefore, notAfter);
        File tmpP12File = File.createTempFile("ssl-", ".p12");
        LOG.debug("p12 file: " + tmpP12File.getAbsolutePath());
        persistKey(tmpP12File, keyPair.getPrivate(), this.certificate, "secret".toCharArray(), "secret".toCharArray());
        SslSocketConnector sslSocketConnector = new SslSocketConnector();
        sslSocketConnector.setKeystore(tmpP12File.getAbsolutePath());
        sslSocketConnector.setTruststore(tmpP12File.getAbsolutePath());
        sslSocketConnector.setTruststoreType("pkcs12");
        sslSocketConnector.setKeystoreType("pkcs12");
        sslSocketConnector.setPassword("secret");
        sslSocketConnector.setKeyPassword("secret");
        sslSocketConnector.setTrustPassword("secret");
        sslSocketConnector.setMaxIdleTime(30000);
        int sslPort = getFreePort();
        sslSocketConnector.setPort(sslPort);
        this.servletTester.getContext().getServer().addConnector(sslSocketConnector);
        this.sslLocation = "https://localhost:" + sslPort + "/";
        this.servletTester.start();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager trustManager = new TestTrustManager(this.certificate);
        sslContext.init(null, new TrustManager[] { trustManager }, null);
        SSLContext.setDefault(sslContext);
    }
