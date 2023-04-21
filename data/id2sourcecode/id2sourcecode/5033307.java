    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if (e.getChannel().isConnected()) {
            logger.warn("Exception {}", e.getCause().getMessage(), e.getCause());
            if (e.getCause() instanceof ClosedChannelException) {
                return;
            }
            status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
            sendError(ctx.getChannel(), "Exception get: " + e.getCause().getMessage());
        }
    }
