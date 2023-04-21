    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws OpenR66ProtocolNetworkException {
        final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
        if (sslHandler != null) {
            ChannelFuture handshakeFuture;
            handshakeFuture = sslHandler.handshake();
            handshakeFuture.addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.debug("Handshake: " + future.isSuccess(), future.getCause());
                    if (future.isSuccess()) {
                        setStatusSslConnectedChannel(future.getChannel(), true);
                    } else {
                        if (Configuration.configuration.r66Mib != null) {
                            String error2 = future.getCause() != null ? future.getCause().getMessage() : "During Handshake";
                            Configuration.configuration.r66Mib.notifyError("SSL Connection Error", error2);
                        }
                        setStatusSslConnectedChannel(future.getChannel(), false);
                        future.getChannel().close();
                    }
                }
            });
        } else {
            logger.error("SSL Not found");
        }
        super.channelConnected(ctx, e);
    }
