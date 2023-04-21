    public void connect() throws RougeConnectionFailure {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        this.channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            throw new RougeConnectionFailure(future.getCause());
        }
    }
