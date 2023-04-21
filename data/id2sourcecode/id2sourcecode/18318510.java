    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + TelnetClient.class.getSimpleName() + " <host> <port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new TelnetClientPipelineFactory());
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }
        ChannelFuture lastWriteFuture = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            lastWriteFuture = channel.write(line + "\r\n");
            if (line.toLowerCase().equals("bye")) {
                channel.getCloseFuture().awaitUninterruptibly();
                break;
            }
        }
        if (lastWriteFuture != null) {
            lastWriteFuture.awaitUninterruptibly();
        }
        channel.close().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }
