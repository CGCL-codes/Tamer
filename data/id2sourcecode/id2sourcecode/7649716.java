    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        if (exception.compareAndSet(null, e.getCause())) {
            e.getChannel().close();
        }
    }
