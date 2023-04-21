        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            Channel c = ctx.getChannel();
            c.setReadable(false);
            c.getPipeline().remove(ClientChannelHandler.class);
            InternetEndpoint remote_ep = new InternetEndpoint((InetSocketAddress) c.getRemoteAddress());
            InternetLink link = new InternetLink(InternetLinkManager.this, getLocalEndpoint(), remote_ep, c, getExecutor());
            fm.completeFuture(link);
        }
