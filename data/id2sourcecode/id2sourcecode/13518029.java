    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Please specify document root directory");
            System.exit(1);
        }
        ClassLoader cl = NHttpSSLServer.class.getClassLoader();
        URL url = cl.getResource("test.keystore");
        KeyStore keystore = KeyStore.getInstance("jks");
        keystore.load(url.openStream(), "nopassword".toCharArray());
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, "nopassword".toCharArray());
        KeyManager[] keymanagers = kmfactory.getKeyManagers();
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(keymanagers, null, null);
        HttpParams params = new BasicHttpParams();
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000).setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024).setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false).setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true).setParameter(CoreProtocolPNames.ORIGIN_SERVER, "Jakarta-HttpComponents-NIO/1.1");
        BasicHttpProcessor httpproc = new BasicHttpProcessor();
        httpproc.addInterceptor(new ResponseDate());
        httpproc.addInterceptor(new ResponseServer());
        httpproc.addInterceptor(new ResponseContent());
        httpproc.addInterceptor(new ResponseConnControl());
        BufferingHttpServiceHandler handler = new BufferingHttpServiceHandler(httpproc, new DefaultHttpResponseFactory(), new DefaultConnectionReuseStrategy(), params);
        HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
        reqistry.register("*", new HttpFileHandler(args[0]));
        handler.setHandlerResolver(reqistry);
        handler.setEventListener(new EventLogger());
        IOEventDispatch ioEventDispatch = new SSLServerIOEventDispatch(handler, sslcontext, params);
        ListeningIOReactor ioReactor = new DefaultListeningIOReactor(2, params);
        try {
            ioReactor.listen(new InetSocketAddress(8080));
            ioReactor.execute(ioEventDispatch);
        } catch (InterruptedIOException ex) {
            System.err.println("Interrupted");
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
        System.out.println("Shutdown");
    }
