    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        Throwable e1 = e.getCause();
        Channel channel = e.getChannel();
        if (session == null) {
            logger.warn("NO SESSION", e1);
            return;
        }
        if (e1 instanceof ConnectException) {
            ConnectException e2 = (ConnectException) e1;
            logger.warn("Connection impossible since {} with Channel {}", e2.getMessage(), e.getChannel());
        } else if (e1 instanceof ChannelException) {
            ChannelException e2 = (ChannelException) e1;
            logger.warn("Connection (example: timeout) impossible since {} with Channel {}", e2.getMessage(), e.getChannel());
        } else if (e1 instanceof ClosedChannelException) {
            logger.debug("Connection closed before end");
        } else if (e1 instanceof CommandAbstractException) {
            CommandAbstractException e2 = (CommandAbstractException) e1;
            logger.warn("Command Error Reply {}", e2.getMessage());
            session.setReplyCode(e2);
            businessHandler.afterRunCommandKo(e2);
            if (channel.isConnected()) {
                writeFinalAnswer();
            }
            return;
        } else if (e1 instanceof NullPointerException) {
            NullPointerException e2 = (NullPointerException) e1;
            logger.warn("Null pointer Exception", e2);
            try {
                if (session != null) {
                    session.setExitErrorCode("Internal error: disconnect");
                    if (businessHandler != null && session.getDataConn() != null) {
                        businessHandler.exceptionLocalCaught(e);
                        if (channel.isConnected()) {
                            writeFinalAnswer();
                        }
                    }
                }
            } catch (NullPointerException e3) {
            }
            return;
        } else if (e1 instanceof IOException) {
            IOException e2 = (IOException) e1;
            logger.warn("Connection aborted since {} with Channel {}", e2.getMessage(), e.getChannel());
        } else {
            logger.warn("Unexpected exception from downstream" + " Ref Channel: {}" + e.getChannel().toString(), e1.getMessage());
        }
        session.setExitErrorCode("Internal error: disconnect");
        businessHandler.exceptionLocalCaught(e);
        if (channel.isConnected()) {
            writeFinalAnswer();
        }
    }
