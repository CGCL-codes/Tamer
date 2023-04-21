    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        log.error("Unexpected exception from downstream.");
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
