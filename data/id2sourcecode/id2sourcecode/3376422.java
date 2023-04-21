    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws OpenR66Exception {
        final AbstractLocalPacket packet = (AbstractLocalPacket) e.getMessage();
        if (packet.getType() == LocalPacketFactory.STARTUPPACKET) {
            startup(e.getChannel(), (StartupPacket) packet);
        } else {
            if (localChannelReference == null) {
                logger.error("No LocalChannelReference at " + packet.getClass().getName());
                session.newState(ERROR);
                final ErrorPacket errorPacket = new ErrorPacket("No LocalChannelReference at " + packet.getClass().getName(), ErrorCode.ConnectionImpossible.getCode(), ErrorPacket.FORWARDCLOSECODE);
                Channels.write(e.getChannel(), errorPacket).awaitUninterruptibly();
                localChannelReference.invalidateRequest(new R66Result(new OpenR66ProtocolSystemException("No LocalChannelReference"), session, true, ErrorCode.ConnectionImpossible, null));
                ChannelUtils.close(e.getChannel());
                if (Configuration.configuration.r66Mib != null) {
                    Configuration.configuration.r66Mib.notifyWarning("No LocalChannelReference", packet.getClass().getSimpleName());
                }
                return;
            }
            switch(packet.getType()) {
                case LocalPacketFactory.AUTHENTPACKET:
                    {
                        authent(e.getChannel(), (AuthentPacket) packet);
                        break;
                    }
                case LocalPacketFactory.DATAPACKET:
                    {
                        session.newState(DATAR);
                        data(e.getChannel(), (DataPacket) packet);
                        break;
                    }
                case LocalPacketFactory.VALIDPACKET:
                    {
                        valid(e.getChannel(), (ValidPacket) packet);
                        break;
                    }
                case LocalPacketFactory.ERRORPACKET:
                    {
                        session.newState(ERROR);
                        errorMesg(e.getChannel(), (ErrorPacket) packet);
                        break;
                    }
                case LocalPacketFactory.CONNECTERRORPACKET:
                    {
                        connectionError(e.getChannel(), (ConnectionErrorPacket) packet);
                        break;
                    }
                case LocalPacketFactory.REQUESTPACKET:
                    {
                        request(e.getChannel(), (RequestPacket) packet);
                        break;
                    }
                case LocalPacketFactory.SHUTDOWNPACKET:
                    {
                        session.newState(SHUTDOWN);
                        shutdown(e.getChannel(), (ShutdownPacket) packet);
                        break;
                    }
                case LocalPacketFactory.STOPPACKET:
                case LocalPacketFactory.CANCELPACKET:
                case LocalPacketFactory.CONFIMPORTPACKET:
                case LocalPacketFactory.CONFEXPORTPACKET:
                case LocalPacketFactory.BANDWIDTHPACKET:
                    {
                        logger.error("Unimplemented Mesg: " + packet.getClass().getName());
                        session.newState(ERROR);
                        localChannelReference.invalidateRequest(new R66Result(new OpenR66ProtocolSystemException("Not implemented"), session, true, ErrorCode.Unimplemented, null));
                        final ErrorPacket errorPacket = new ErrorPacket("Unimplemented Mesg: " + packet.getClass().getName(), ErrorCode.Unimplemented.getCode(), ErrorPacket.FORWARDCLOSECODE);
                        ChannelUtils.writeAbstractLocalPacket(localChannelReference, errorPacket).awaitUninterruptibly();
                        ChannelUtils.close(e.getChannel());
                        break;
                    }
                case LocalPacketFactory.TESTPACKET:
                    {
                        session.newState(TEST);
                        test(e.getChannel(), (TestPacket) packet);
                        break;
                    }
                case LocalPacketFactory.ENDTRANSFERPACKET:
                    {
                        endTransfer(e.getChannel(), (EndTransferPacket) packet);
                        break;
                    }
                case LocalPacketFactory.INFORMATIONPACKET:
                    {
                        session.newState(INFORMATION);
                        information(e.getChannel(), (InformationPacket) packet);
                        break;
                    }
                case LocalPacketFactory.ENDREQUESTPACKET:
                    {
                        endRequest(e.getChannel(), (EndRequestPacket) packet);
                        break;
                    }
                case LocalPacketFactory.BUSINESSREQUESTPACKET:
                    {
                        businessRequest(e.getChannel(), (BusinessRequestPacket) packet);
                        break;
                    }
                default:
                    {
                        logger.error("Unknown Mesg: " + packet.getClass().getName());
                        session.newState(ERROR);
                        localChannelReference.invalidateRequest(new R66Result(new OpenR66ProtocolSystemException("Unknown Message"), session, true, ErrorCode.Unimplemented, null));
                        final ErrorPacket errorPacket = new ErrorPacket("Unkown Mesg: " + packet.getClass().getName(), ErrorCode.Unimplemented.getCode(), ErrorPacket.FORWARDCLOSECODE);
                        ChannelUtils.writeAbstractLocalPacket(localChannelReference, errorPacket).awaitUninterruptibly();
                        ChannelUtils.close(e.getChannel());
                    }
            }
        }
    }
