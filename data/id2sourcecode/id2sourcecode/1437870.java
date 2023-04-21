    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        if (e.getChannel().isConnected()) {
            e.getChannel().close();
        }
        Globals.getInstance().getForwarder().fireExceptionCaught(ctx, e);
    }
