    @Override
    public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent e) {
        RtmpServer.CHANNELS.add(e.getChannel());
        logger.info("opened channel: {}", e);
    }
