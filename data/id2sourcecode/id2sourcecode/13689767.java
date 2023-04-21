    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.log(Level.SEVERE, "Unexpected exception from downStream", e.getCause());
        e.getChannel().close().awaitUninterruptibly();
    }
