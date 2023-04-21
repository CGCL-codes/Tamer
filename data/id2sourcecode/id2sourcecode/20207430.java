    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        final Channel inboundChannel = e.getChannel();
        inboundChannel.setReadable(false);
        ClientBootstrap client = new ClientBootstrap(cf);
        client.getPipeline().addLast("handler", new OutboundHandler(e.getChannel()));
        ChannelFuture future = client.connect(new InetSocketAddress(remoteHost, remotePort));
        outboundChannel = future.getChannel();
        future.addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    inboundChannel.setReadable(true);
                } else {
                    inboundChannel.close();
                }
            }
        });
    }
