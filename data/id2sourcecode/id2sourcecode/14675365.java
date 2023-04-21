    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        Channel ch = e.getChannel();
        Throwable cause = e.getCause();
        if (cause instanceof TooLongFrameException) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if (cause != null && !cause.getClass().equals(ClosedChannelException.class) && !cause.getClass().equals(IOException.class)) {
            cause.printStackTrace();
        }
        if (ch.isConnected()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
        e.getChannel().close();
    }
