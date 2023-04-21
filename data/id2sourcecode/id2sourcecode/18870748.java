    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + HttpClient.class.getSimpleName() + " <URL>");
            return;
        }
        URI uri = new URI(args[0]);
        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        String host = uri.getHost() == null ? "localhost" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if (scheme.equalsIgnoreCase("http")) {
                port = 80;
            } else if (scheme.equalsIgnoreCase("https")) {
                port = 443;
            }
        }
        if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
            System.err.println("Only HTTP(S) is supported.");
            return;
        }
        boolean ssl = scheme.equalsIgnoreCase("https");
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new HttpClientPipelineFactory(ssl));
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString());
        request.setHeader(HttpHeaders.Names.HOST, host);
        request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
        CookieEncoder httpCookieEncoder = new CookieEncoder(false);
        httpCookieEncoder.addCookie("my-cookie", "foo");
        httpCookieEncoder.addCookie("another-cookie", "bar");
        request.setHeader(HttpHeaders.Names.COOKIE, httpCookieEncoder.encode());
        channel.write(request);
        channel.getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }
