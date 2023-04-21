    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.log(Level.WARNING, "Unexpected exception from downStream", e.getCause());
        e.getChannel().close().awaitUninterruptibly();
        for (SessionEndListener listener : listeners) {
            listener.sessionTerminated(this);
        }
        super.exceptionCaught(ctx, e);
    }
