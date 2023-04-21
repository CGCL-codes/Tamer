    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        if (e.getChannel().isConnected()) {
            e.getChannel().close();
        }
        Globals.getInstance().getClient().fireExceptionCaught(ctx, e);
    }
