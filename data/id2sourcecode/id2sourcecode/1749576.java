    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        SessionManager.logger.info("Connection from " + ctx.getChannel().getRemoteAddress().toString() + " opened.");
    }
