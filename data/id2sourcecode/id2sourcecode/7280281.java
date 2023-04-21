    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        if (NetworkTransaction.getNbLocalChannel(e.getChannel()) > 0) {
            logger.debug("Network Channel Closed: {} LocalChannels Left: {}", e.getChannel().getId(), NetworkTransaction.getNbLocalChannel(e.getChannel()));
            try {
                Thread.sleep(Configuration.WAITFORNETOP);
            } catch (InterruptedException e1) {
            }
            Configuration.configuration.getLocalTransaction().closeLocalChannelsFromNetworkChannel(e.getChannel());
        }
        NetworkTransaction.removeForceNetworkChannel(e.getChannel());
        if (dbSession != null && dbSession.internalId != DbConstant.admin.session.internalId) {
            dbSession.disconnect();
        }
    }
