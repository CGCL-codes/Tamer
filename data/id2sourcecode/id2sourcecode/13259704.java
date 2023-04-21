    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        Throwable t = e.getCause();
        if (t instanceof CorruptedFrameException || t instanceof TooLongFrameException) {
            LOG.error("Corrupted fram recieved from bookie: " + e.getChannel().getRemoteAddress());
            return;
        }
        if (t instanceof IOException) {
            return;
        }
        LOG.fatal("Unexpected exception caught by bookie client channel handler", t);
    }
