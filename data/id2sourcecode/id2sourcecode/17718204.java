    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Channel channel = e.getChannel();
        for (int i = 0; i < FtpInternalConfiguration.RETRYNB; i++) {
            session = configuration.getFtpSession(channel, isActive);
            if (session == null) {
                logger.warn("Session not found at try " + i);
                try {
                    Thread.sleep(FtpInternalConfiguration.RETRYINMS);
                } catch (InterruptedException e1) {
                    break;
                }
            } else {
                break;
            }
        }
        if (session == null) {
            logger.error("Session not found!");
            Channels.close(channel);
            return;
        }
        channelPipeline = ctx.getPipeline();
        dataChannel = channel;
        dataBusinessHandler.setFtpSession(getFtpSession());
        FtpChannelUtils.addDataChannel(channel, session.getConfiguration());
        if (isStillAlive()) {
            setCorrectCodec();
            session.getDataConn().getFtpTransferControl().setOpenedDataChannel(channel, this);
        } else {
            session.getDataConn().getFtpTransferControl().setOpenedDataChannel(null, this);
            return;
        }
        isReady = true;
    }
