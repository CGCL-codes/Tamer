    protected ChannelFuture getRemoteFuture() {
        if (null != remoteFuture && remoteFuture.getChannel().isConnected()) {
            return remoteFuture;
        }
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", new HttpResponseDecoder());
        pipeline.addLast("handler", new RemoteChannelResponseHandler());
        SocketChannel channel = getClientSocketChannelFactory().newChannel(pipeline);
        channel.getConfig().setOption("connectTimeoutMillis", 40 * 1000);
        ChannelFuture future = channel.connect(new InetSocketAddress(host, port));
        return future;
    }
