    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        LOG.info("ProxyHandler#channelOpen");
        final Channel inboundChannel = e.getChannel();
        inboundChannel.setReadable(false);
        ClientBootstrap cb = new ClientBootstrap(this.clientChannelFactory);
        cb.setOption("broadcast", "false");
        cb.getPipeline().addLast("handler", new OutboundHandler(inboundChannel));
        ChannelFuture f = cb.connect(this.config.getForwarders().iterator().next());
        this.outboundChannel = f.getChannel();
        f.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    inboundChannel.setReadable(true);
                } else {
                    inboundChannel.close();
                }
            }
        });
    }
