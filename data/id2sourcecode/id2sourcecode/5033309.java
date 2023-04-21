    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ChannelGroup group = Configuration.getHttpChannelGroup();
        if (group != null) {
            group.add(e.getChannel());
        }
        super.channelConnected(ctx, e);
    }
