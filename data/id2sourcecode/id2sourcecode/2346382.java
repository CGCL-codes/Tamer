    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        OpenR66Exception exception = OpenR66ExceptionTrappedFactory.getExceptionFromTrappedException(e.getChannel(), e);
        if (exception != null) {
            if (!(exception instanceof OpenR66ProtocolBusinessNoWriteBackException)) {
                if (e.getCause() instanceof IOException) {
                    return;
                }
                logger.warn("Exception in HttpSslHandler {}", exception.getMessage());
            }
            if (e.getChannel().isConnected()) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            }
        } else {
            return;
        }
    }
