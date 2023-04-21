    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.debug("Local Client Channel Closed: {}", e.getChannel().getId());
    }
