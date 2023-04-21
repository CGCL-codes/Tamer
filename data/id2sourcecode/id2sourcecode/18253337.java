    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getChannel().close();
        LOG.error(e.toString(), e.getCause());
        coordinationStatus.setStatus(STATUS.UNKOWN_ERROR, e.toString());
    }
