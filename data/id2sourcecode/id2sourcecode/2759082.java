    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        initExecClient();
        factory.addChannel(ctx.getChannel());
    }
