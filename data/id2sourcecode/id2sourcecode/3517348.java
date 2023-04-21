    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        System.err.println(responseContent.toString());
        e.getChannel().close();
    }
