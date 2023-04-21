    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        LOGGER.warn("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
