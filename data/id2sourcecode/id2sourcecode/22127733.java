    private static void formget(String get, ArkRequest req, long rank) {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }
        QueryStringEncoder encoder = new QueryStringEncoder(get);
        encoder.addParam(HttpFormattedHandler.ArkArgs.LEG.name(), "111");
        encoder.addParam(HttpFormattedHandler.ArkArgs.STO.name(), "-9151313343288442623");
        encoder.addParam(HttpFormattedHandler.ArkArgs.DID.name(), Long.toString(rank));
        encoder.addParam(HttpFormattedHandler.ArkArgs.CTYPE.name(), "text/plain");
        encoder.addParam(HttpFormattedHandler.ArkArgs.REQTYPE.name(), req.name());
        encoder.addParam("Send", "Send");
        URI uriGet;
        try {
            uriGet = new URI(encoder.toString());
        } catch (URISyntaxException e) {
            System.err.println("Error: " + e.getMessage());
            bootstrap.releaseExternalResources();
            return;
        }
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uriGet.toASCIIString());
        for (Entry<String, String> entry : headers) {
            request.setHeader(entry.getKey(), entry.getValue());
        }
        channel.write(request);
        channel.getCloseFuture().awaitUninterruptibly();
    }
