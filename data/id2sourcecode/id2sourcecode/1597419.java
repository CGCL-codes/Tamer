    protected void sendRequest() {
        ChannelFactory factory = new NioDatagramChannelFactory(Executors.newSingleThreadExecutor());
        try {
            ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(factory);
            bootstrap.getPipeline().addLast("handler", DNSClient.this);
            bootstrap.setOption("broadcast", "false");
            ChannelFuture future = bootstrap.connect(this.serverAddress);
            future.awaitUninterruptibly(30, TimeUnit.SECONDS);
            if (future.isSuccess() == false) {
                future.getCause().printStackTrace();
            }
            future.getChannel().getCloseFuture().awaitUninterruptibly();
        } finally {
            factory.releaseExternalResources();
        }
    }
