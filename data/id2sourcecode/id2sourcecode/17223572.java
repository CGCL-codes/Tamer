    public void connect(final ClientOptions options) {
        final ClientBootstrap bootstrap = getBootstrap(Executors.newCachedThreadPool(), options);
        final ChannelFuture future = bootstrap.connect(new InetSocketAddress(options.getHost(), options.getPort()));
        future.awaitUninterruptibly();
        if (!future.isSuccess()) {
            logger.error("error creating client connection: {}", future.getCause().getMessage());
        }
        clientChannel = future.getChannel();
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.getFactory().releaseExternalResources();
    }
