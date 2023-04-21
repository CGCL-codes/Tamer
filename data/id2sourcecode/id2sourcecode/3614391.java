    private NetworkChannel createNewConnection(SocketAddress socketServerAddress, boolean isSSL) throws OpenR66ProtocolNetworkException, OpenR66ProtocolRemoteShutdownException, OpenR66ProtocolNoConnectionException {
        if (!isAddressValid(socketServerAddress)) {
            throw new OpenR66ProtocolRemoteShutdownException("Cannot connect to remote server since it is shutting down");
        }
        NetworkChannel networkChannel;
        try {
            networkChannel = getRemoteChannel(socketServerAddress);
        } catch (OpenR66ProtocolNoDataException e1) {
            networkChannel = null;
        }
        if (networkChannel != null) {
            logger.debug("Already Connected: {}", networkChannel);
            networkChannel.count++;
            return networkChannel;
        }
        ChannelFuture channelFuture = null;
        for (int i = 0; i < Configuration.RETRYNB; i++) {
            if (isSSL) {
                if (Configuration.configuration.HOST_SSLID != null) {
                    channelFuture = clientSslBootstrap.connect(socketServerAddress);
                } else {
                    throw new OpenR66ProtocolNoConnectionException("No SSL support");
                }
            } else {
                channelFuture = clientBootstrap.connect(socketServerAddress);
            }
            try {
                channelFuture.await();
            } catch (InterruptedException e1) {
            }
            if (channelFuture.isSuccess()) {
                final Channel channel = channelFuture.getChannel();
                if (isSSL) {
                    if (!NetworkSslServerHandler.isSslConnectedChannel(channel)) {
                        logger.debug("KO CONNECT since SSL handshake is over");
                        Channels.close(channel);
                        throw new OpenR66ProtocolNoConnectionException("Cannot finish connect to remote server");
                    }
                }
                networkChannelGroup.add(channel);
                return putRemoteChannel(channel);
            } else {
                try {
                    Thread.sleep(Configuration.WAITFORNETOP);
                } catch (InterruptedException e) {
                }
                if (!channelFuture.isDone()) {
                    throw new OpenR66ProtocolNoConnectionException("Cannot connect to remote server due to interruption");
                }
                if (channelFuture.getCause() instanceof ConnectException) {
                    logger.debug("KO CONNECT:" + channelFuture.getCause().getMessage());
                    throw new OpenR66ProtocolNoConnectionException("Cannot connect to remote server", channelFuture.getCause());
                } else {
                    logger.debug("KO CONNECT but retry", channelFuture.getCause());
                }
            }
            try {
                Thread.sleep(Configuration.WAITFORNETOP);
            } catch (InterruptedException e) {
                throw new OpenR66ProtocolNetworkException("Cannot connect to remote server", e);
            }
        }
        throw new OpenR66ProtocolNetworkException("Cannot connect to remote server", channelFuture.getCause());
    }
