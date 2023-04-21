    public NettyRpcChannel blockingConnect(SocketAddress sa) {
        return new NettyRpcChannel(bootstrap.connect(sa).awaitUninterruptibly().getChannel());
    }
