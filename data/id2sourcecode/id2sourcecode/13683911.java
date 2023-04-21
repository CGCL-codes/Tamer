    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
        state = State.CONNECTED;
        inetAddress = ((InetSocketAddress) e.getChannel().getRemoteAddress()).getAddress();
        channel = ctx.getChannel();
        log.info("Channel connected Ip:" + inetAddress.getHostAddress());
    }
