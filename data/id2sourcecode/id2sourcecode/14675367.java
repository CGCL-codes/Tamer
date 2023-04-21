    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelOpen(ctx, e);
        if (group != null) {
            group.add(ctx.getChannel());
        }
    }
