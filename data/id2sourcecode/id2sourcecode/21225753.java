    public static void main(String[] args) throws Exception {
        if (args.length < 2 || args.length > 3) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port> [<first message size>]");
            return;
        }
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        final int firstMessageSize;
        if (args.length == 3) {
            firstMessageSize = Integer.parseInt(args[2]);
        } else {
            firstMessageSize = 256;
        }
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new EchoClientHandler(firstMessageSize));
            }
        });
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }
