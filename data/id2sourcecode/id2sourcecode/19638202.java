    @Override
    public void exceptionCaught(ChannelHandlerContext chc, ExceptionEvent e) throws Exception {
        log.error("Channel错误", e.getCause());
        e.getChannel().close();
    }
