    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.factory.removeChannel(e.getChannel());
    }
