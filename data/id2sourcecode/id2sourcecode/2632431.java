    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        if (answered) {
            logger.debug("Exception while answered: ", e.getCause());
        } else {
            logger.error("Unexpected exception from downstream while not answered.", e.getCause());
        }
        Throwable e1 = e.getCause();
        if (e1 instanceof CancelledKeyException) {
        } else if (e1 instanceof ClosedChannelException) {
        } else if (e1 instanceof NullPointerException) {
            if (e.getChannel().isConnected()) {
                e.getChannel().close();
            }
        } else if (e1 instanceof IOException) {
            if (e.getChannel().isConnected()) {
                e.getChannel().close();
            }
        } else if (e1 instanceof RejectedExecutionException) {
            if (e.getChannel().isConnected()) {
                e.getChannel().close();
            }
        }
    }
