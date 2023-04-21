    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Channel channel = e.getChannel();
        controlChannel = channel;
        session.setControlConnected();
        FtpChannelUtils.addCommandChannel(channel, session.getConfiguration());
        if (isStillAlive()) {
            AbstractCommand command = new ConnectionCommand(getFtpSession());
            session.setNextCommand(command);
            businessHandler.executeChannelConnected(channel);
            messageRunAnswer();
            getFtpSession().setReady(true);
        }
    }
