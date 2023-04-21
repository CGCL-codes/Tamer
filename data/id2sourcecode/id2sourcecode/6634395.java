    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + HttpTunnelingClientExample.class.getSimpleName() + " <URL>");
            System.err.println("Example: " + HttpTunnelingClientExample.class.getSimpleName() + " http://localhost:8080/netty-tunnel");
            return;
        }
        URI uri = new URI(args[0]);
        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        ClientBootstrap b = new ClientBootstrap(new HttpTunnelingClientSocketChannelFactory(new OioClientSocketChannelFactory(Executors.newCachedThreadPool())));
        b.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new StringDecoder(), new StringEncoder(), new LoggingHandler(InternalLogLevel.INFO));
            }
        });
        b.setOption("serverName", uri.getHost());
        b.setOption("serverPath", uri.getRawPath());
        if (scheme.equals("https")) {
            b.setOption("sslContext", SecureChatSslContextFactory.getClientContext());
        } else if (!scheme.equals("http")) {
            System.err.println("Only HTTP(S) is supported.");
            return;
        }
        ChannelFuture channelFuture = b.connect(new InetSocketAddress(uri.getHost(), uri.getPort()));
        channelFuture.awaitUninterruptibly();
        System.out.println("Enter text ('quit' to exit)");
        ChannelFuture lastWriteFuture = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            String line = in.readLine();
            if (line == null || "quit".equalsIgnoreCase(line)) {
                break;
            }
            lastWriteFuture = channelFuture.getChannel().write(line);
        }
        if (lastWriteFuture != null) {
            lastWriteFuture.awaitUninterruptibly();
        }
        channelFuture.getChannel().close();
        channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();
        b.releaseExternalResources();
    }
