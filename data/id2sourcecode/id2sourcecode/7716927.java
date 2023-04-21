    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.getChannel().close().addListener(MUSUser.REPORT_CLOSE);
    }
