    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.debug("Connected");
        final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
        if (sslHandler != null) {
            ChannelFuture handshakeFuture;
            handshakeFuture = sslHandler.handshake();
            if (handshakeFuture != null) {
                handshakeFuture.addListener(new ChannelFutureListener() {

                    public void operationComplete(ChannelFuture future) throws Exception {
                        logger.debug("Handshake: " + future.isSuccess(), future.getCause());
                        if (future.isSuccess()) {
                            setStatusSslConnectedChannel(future.getChannel(), true);
                        } else {
                            setStatusSslConnectedChannel(future.getChannel(), false);
                            future.getChannel().close();
                        }
                    }
                });
            }
        } else {
            logger.warn("SSL Not found");
        }
        super.channelConnected(ctx, e);
        ChannelGroup group = Configuration.configuration.getHttpChannelGroup();
        if (group != null) {
            group.add(e.getChannel());
        }
    }
