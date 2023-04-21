    public TestHttpSSLServer(final HttpParams params) throws Exception {
        super();
        this.params = params;
        this.ioReactor = new DefaultListeningIOReactor(2, this.params);
        ClassLoader cl = getClass().getClassLoader();
        URL url = cl.getResource("test.keystore");
        KeyStore keystore = KeyStore.getInstance("jks");
        keystore.load(url.openStream(), "nopassword".toCharArray());
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, "nopassword".toCharArray());
        KeyManager[] keymanagers = kmfactory.getKeyManagers();
        this.sslcontext = SSLContext.getInstance("TLS");
        this.sslcontext.init(keymanagers, null, null);
    }
