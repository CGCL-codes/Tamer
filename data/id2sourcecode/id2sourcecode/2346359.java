    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = e.getChannel();
        logger.debug("Add channel to ssl");
        addSslConnectedChannel(channel);
        Configuration.configuration.getHttpChannelGroup().add(channel);
        super.channelOpen(ctx, e);
    }
