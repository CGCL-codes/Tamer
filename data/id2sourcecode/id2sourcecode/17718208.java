    @Override
    public void channelInterestChanged(ChannelHandlerContext arg0, ChannelStateEvent arg1) {
        int op = arg1.getChannel().getInterestOps();
        if (op == Channel.OP_NONE || op == Channel.OP_READ) {
            if (isReady) {
                session.getDataConn().getFtpTransferControl().runTrueRetrieve();
            }
        }
    }
