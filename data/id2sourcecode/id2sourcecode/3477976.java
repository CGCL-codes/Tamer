    @Override
    public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent e) {
        RtmpServer.ALL_CHANNELS.add(e.getChannel());
        logger.info("opened channel: {}", e);
    }
