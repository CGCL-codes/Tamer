    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (isShutdown(ctx.getChannel())) {
            answered = true;
            return;
        }
        answered = false;
        factory.addChannel(ctx.getChannel());
    }
