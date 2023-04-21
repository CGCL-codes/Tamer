    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (localChannelReference == null) {
            initLocalClientHandler(e.getChannel());
        }
        final AbstractLocalPacket packet = (AbstractLocalPacket) e.getMessage();
        if (packet.getType() != LocalPacketFactory.STARTUPPACKET) {
            logger.error("Local Client Channel Recv wrong packet: " + e.getChannel().getId() + " : " + packet.toString());
            throw new OpenR66ProtocolSystemException("Should not be here");
        }
        logger.debug("LocalClientHandler initialized: " + (localChannelReference != null));
    }
