    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        final Channel inboundChannel = e.getChannel();
        RtmpProxy.ALL_CHANNELS.add(inboundChannel);
        inboundChannel.setReadable(false);
        ClientBootstrap cb = new ClientBootstrap(cf);
        cb.getPipeline().addLast("handshaker", new RtmpProxyHandshakeHandler());
        cb.getPipeline().addLast("handler", new OutboundHandler(e.getChannel()));
        ChannelFuture f = cb.connect(new InetSocketAddress(remoteHost, remotePort));
        outboundChannel = f.getChannel();
        f.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.info("connected to remote host: {}, port: {}", remoteHost, remotePort);
                    inboundChannel.setReadable(true);
                } else {
                    inboundChannel.close();
                }
            }
        });
    }
