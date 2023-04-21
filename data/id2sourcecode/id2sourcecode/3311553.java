    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.log(Level.WARN, "Exception from downstream.", e.getCause());
        if (e.getCause() instanceof ReadTimeoutException) {
            e.getChannel().close();
        } else if (e.getCause() instanceof ClosedChannelException) {
            e.getChannel().close();
        } else {
            e.getChannel().write(e.getCause().getMessage() + "\r\n");
        }
    }
