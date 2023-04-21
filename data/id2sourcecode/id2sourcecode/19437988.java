    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
        Server.getChannelsArray().remove(e.getChannel().getId());
        Globals.getInstance().getServer().fireExceptionCaught(ctx, e);
    }
