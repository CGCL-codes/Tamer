    @Override
    public final void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Channel channel = e.getChannel();
        ConnectionHandler connection = new ConnectionHandler(channel);
        ctx.setAttachment(connection);
    }
