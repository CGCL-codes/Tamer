    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        sessions.add(this);
        addEndListener(server);
        connection = e.getChannel();
        start();
        super.channelConnected(ctx, e);
    }
