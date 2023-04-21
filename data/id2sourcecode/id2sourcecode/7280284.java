    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        final NetworkPacket packet = (NetworkPacket) e.getMessage();
        if (packet.getCode() == LocalPacketFactory.CONNECTERRORPACKET) {
            logger.debug("NetworkRecv: {}", packet);
            if (packet.getLocalId() == ChannelUtils.NOCHANNEL) {
                logger.error("Will close NETWORK channel, Cannot continue connection with remote Host: " + packet.toString() + " : " + e.getChannel().getRemoteAddress());
                Channels.close(e.getChannel());
                return;
            }
        } else if (packet.getCode() == LocalPacketFactory.KEEPALIVEPACKET) {
            keepAlivedSent = false;
            try {
                KeepAlivePacket keepAlivePacket = (KeepAlivePacket) LocalPacketCodec.decodeNetworkPacket(packet.getBuffer());
                if (keepAlivePacket.isToValidate()) {
                    keepAlivePacket.validate();
                    NetworkPacket response = new NetworkPacket(ChannelUtils.NOCHANNEL, ChannelUtils.NOCHANNEL, keepAlivePacket);
                    logger.debug("Answer KAlive");
                    Channels.write(e.getChannel(), response);
                } else {
                    logger.debug("Get KAlive");
                }
            } catch (OpenR66ProtocolPacketException e1) {
            }
            return;
        }
        LocalChannelReference localChannelReference = null;
        if (packet.getLocalId() == ChannelUtils.NOCHANNEL) {
            logger.debug("NetworkRecv Create: {} {}", packet, e.getChannel().getId());
            try {
                localChannelReference = NetworkTransaction.createConnectionFromNetworkChannelStartup(e.getChannel(), packet);
            } catch (OpenR66ProtocolSystemException e1) {
                logger.error("Cannot create LocalChannel for: " + packet + " due to " + e1.getMessage());
                NetworkTransaction.removeNetworkChannel(e.getChannel(), null);
                final ConnectionErrorPacket error = new ConnectionErrorPacket("Cannot connect to localChannel since cannot create it", null);
                writeError(e.getChannel(), packet.getRemoteId(), packet.getLocalId(), error);
                return;
            } catch (OpenR66ProtocolRemoteShutdownException e1) {
                logger.warn("Will Close Local from Network Channel");
                Configuration.configuration.getLocalTransaction().closeLocalChannelsFromNetworkChannel(e.getChannel());
                Channels.close(e.getChannel());
                return;
            }
        } else {
            if (packet.getCode() == LocalPacketFactory.ENDREQUESTPACKET) {
                try {
                    localChannelReference = Configuration.configuration.getLocalTransaction().getClient(packet.getRemoteId(), packet.getLocalId());
                } catch (OpenR66ProtocolSystemException e1) {
                    try {
                        logger.debug("Cannot get LocalChannel while an end of request comes: {}", LocalPacketCodec.decodeNetworkPacket(packet.getBuffer()));
                    } catch (OpenR66ProtocolPacketException e2) {
                        logger.debug("Cannot get LocalChannel while an end of request comes: {}", packet.toString());
                    }
                    return;
                }
            } else if (packet.getCode() == LocalPacketFactory.CONNECTERRORPACKET) {
                try {
                    localChannelReference = Configuration.configuration.getLocalTransaction().getClient(packet.getRemoteId(), packet.getLocalId());
                } catch (OpenR66ProtocolSystemException e1) {
                    try {
                        logger.debug("Cannot get LocalChannel while an external error comes: {}", LocalPacketCodec.decodeNetworkPacket(packet.getBuffer()));
                    } catch (OpenR66ProtocolPacketException e2) {
                        logger.debug("Cannot get LocalChannel while an external error comes: {}", packet.toString());
                    }
                    return;
                }
            } else {
                try {
                    localChannelReference = Configuration.configuration.getLocalTransaction().getClient(packet.getRemoteId(), packet.getLocalId());
                } catch (OpenR66ProtocolSystemException e1) {
                    if (NetworkTransaction.isShuttingdownNetworkChannel(e.getChannel())) {
                        return;
                    }
                    logger.debug("Cannot get LocalChannel: " + packet + " due to " + e1.getMessage());
                    final ConnectionErrorPacket error = new ConnectionErrorPacket("Cannot get localChannel since cannot retrieve it", null);
                    writeError(e.getChannel(), packet.getRemoteId(), packet.getLocalId(), error);
                    return;
                }
            }
        }
        Channels.write(localChannelReference.getLocalChannel(), packet.getBuffer());
    }
