    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        SessionManager.logger.warning("Caught exception on connection to " + ctx.getChannel().getRemoteAddress().toString() + " " + e.getCause().toString() + ".");
        for (StackTraceElement ste : e.getCause().getStackTrace()) SessionManager.logger.warning("\tAt " + ste.toString());
        ctx.getChannel().close();
    }
