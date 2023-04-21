    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        logger.info("handshake complete, sending 'connect'");
        writeCommandExpectingResult(e.getChannel(), Command.connect(options));
    }
