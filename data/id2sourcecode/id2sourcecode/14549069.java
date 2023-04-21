    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.debug("Local Client Channel Connected: " + e.getChannel().getId());
    }
