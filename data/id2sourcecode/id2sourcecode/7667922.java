    public void run() {
        if (logger == null) {
            logger = GgInternalLoggerFactory.getLogger(ChangeBandwidthLimits.class);
        }
        ValidPacket valid = new ValidPacket(writeGlobalLimit + " " + readGlobalLimit, writeSessionLimit + " " + readSessionLimit, LocalPacketFactory.BANDWIDTHPACKET);
        DbHostAuth host = Configuration.configuration.HOST_SSLAUTH;
        SocketAddress socketAddress = host.getSocketAddress();
        boolean isSSL = host.isSsl();
        LocalChannelReference localChannelReference = networkTransaction.createConnectionWithRetry(socketAddress, isSSL, future);
        socketAddress = null;
        if (localChannelReference == null) {
            host = null;
            logger.error("Cannot Connect");
            future.setResult(new R66Result(new OpenR66ProtocolNoConnectionException("Cannot connect to server"), null, true, ErrorCode.Internal, null));
            future.setFailure(future.getResult().exception);
            return;
        }
        localChannelReference.sessionNewState(R66FiniteDualStates.VALIDOTHER);
        try {
            ChannelUtils.writeAbstractLocalPacket(localChannelReference, valid);
        } catch (OpenR66ProtocolPacketException e) {
            logger.error("Bad Protocol", e);
            Channels.close(localChannelReference.getLocalChannel());
            localChannelReference = null;
            host = null;
            valid = null;
            future.setResult(new R66Result(e, null, true, ErrorCode.TransferError, null));
            future.setFailure(e);
            return;
        }
        host = null;
        future.awaitUninterruptibly();
        logger.info("Request done with " + (future.isSuccess() ? "success" : "error"));
        Channels.close(localChannelReference.getLocalChannel());
        localChannelReference = null;
    }
