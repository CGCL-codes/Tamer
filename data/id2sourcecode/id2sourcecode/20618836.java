    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = e.getChannel();
        logger.debug("Add channel to ssl");
        addSslConnectedChannel(channel);
        isSSL = true;
        super.channelOpen(ctx, e);
    }
