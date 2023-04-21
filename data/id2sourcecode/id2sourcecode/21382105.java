    public synchronized boolean connectTo(String hostname, int port) {
        if (clientBootstrap == null) {
            logger.fine("Initialization during connectin..");
            init();
        }
        if (channel != null) {
            logger.severe("Already Connected!");
            return false;
        }
        logger.log(Level.INFO, "Cconnecting client to {0} at port {1}", new Object[] { hostname, port });
        ChannelFuture channelFuture = clientBootstrap.connect(new InetSocketAddress(hostname, port));
        if (!channelFuture.awaitUninterruptibly().isSuccess()) {
            clientBootstrap.releaseExternalResources();
            return false;
        }
        channel = channelFuture.getChannel();
        return channel.isConnected();
    }
