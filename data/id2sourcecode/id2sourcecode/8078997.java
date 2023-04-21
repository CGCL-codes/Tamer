    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent evt) throws Exception {
        logger.log(Level.WARNING, "Exception caught, closing channel...", evt.getCause());
        ctx.getChannel().close();
    }
