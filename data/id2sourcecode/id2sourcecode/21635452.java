    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.info("inbound exception: {}", e.getCause().getMessage());
        closeOnFlush(e.getChannel());
    }
