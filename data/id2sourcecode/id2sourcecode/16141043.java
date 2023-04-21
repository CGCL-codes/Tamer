    public void sendRquest(HttpRequest rtspRequest, String host, int port) {
        ChannelFuture future = null;
        if (channel == null || (channel != null && !channel.isConnected())) {
            future = bootstrap.connect(new InetSocketAddress(host, port));
        }
        channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            return;
        }
        channel.write(rtspRequest);
    }
