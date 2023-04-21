    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        Channel ch = e.getChannel();
        MUSUser whatUser = ((SMUSPipeline) ch.getPipeline()).user;
        Throwable cause = e.getCause();
        if (!(cause instanceof ClosedChannelException || cause instanceof IOException)) {
            MUSLog.Log("Netty Exception " + cause + " for " + whatUser, MUSLog.kDeb);
            cause.printStackTrace();
        }
        ch.close().addListener(MUSUser.REPORT_CLOSE);
    }
