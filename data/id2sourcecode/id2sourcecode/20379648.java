    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.log(Level.SEVERE, "Error", e.getCause());
        e.getChannel().close();
    }
