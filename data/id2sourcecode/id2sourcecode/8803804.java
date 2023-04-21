    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent event) {
        logger.debug("channel Connected...");
        NettyMessageServer.allChannelsGroup.add(event.getChannel());
    }
