        @Override
        public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
            link = new Link(ctx.getChannel());
            final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
            sslHandler.handshake(e.getChannel()).addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (!f.isSuccess()) {
                        log.warn("SSL handshake with " + e.getChannel().getRemoteAddress() + " failed!  Closing channel.");
                        e.getChannel().close();
                        return;
                    }
                    listener.linkEstablished(link, client);
                    connectfp.getManager().completeFuture(link);
                }
            });
        }
