    @Override
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
        if (keepAlivedSent) {
            logger.error("Not getting KAlive: closing channel");
            if (Configuration.configuration.r66Mib != null) {
                Configuration.configuration.r66Mib.notifyWarning("KeepAlive get no answer", "Closing network connection");
            }
            ChannelUtils.close(e.getChannel());
        } else {
            keepAlivedSent = true;
            KeepAlivePacket keepAlivePacket = new KeepAlivePacket();
            NetworkPacket response = new NetworkPacket(ChannelUtils.NOCHANNEL, ChannelUtils.NOCHANNEL, keepAlivePacket);
            logger.debug("Write KAlive");
            Channels.write(e.getChannel(), response);
        }
    }
