    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getChannel().close();
        coordinationStatus.setStatus(STATUS.UNKOWN_ERROR, e.toString());
        Throwable error = e.getCause();
        LOG.error(e.toString(), error);
    }
