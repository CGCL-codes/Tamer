    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws OpenR66ProtocolNetworkException {
        try {
            NetworkTransaction.setPassiveNetworkChannel(e.getChannel());
        } catch (OpenR66ProtocolRemoteShutdownException e1) {
            logger.warn("Passive Connectionin error", e1);
        }
        super.channelConnected(ctx, e);
    }
