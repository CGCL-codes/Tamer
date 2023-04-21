    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        Throwable e1 = e.getCause();
        if (!(e1 instanceof CommandAbstractException)) {
            if (e1 instanceof IOException) {
                return;
            }
            logger.warn("Exception in HttpSslHandler", e1);
        }
        if (e.getChannel().isConnected()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
        }
    }
