    public LocalChannelReference createNewClient(Channel networkChannel, Integer remoteId, R66Future futureRequest) throws OpenR66ProtocolSystemException {
        ChannelFuture channelFuture = null;
        logger.debug("Status LocalChannelServer: {} {}", serverChannel.getClass().getName(), serverChannel.getConfig().getConnectTimeoutMillis() + " " + serverChannel.isBound());
        R66Future validLCR = new R66Future(true);
        validLocalChannelHashMap.put(remoteId, validLCR);
        for (int i = 0; i < Configuration.RETRYNB; i++) {
            channelFuture = clientBootstrap.connect(socketLocalServerAddress);
            try {
                channelFuture.await();
            } catch (InterruptedException e1) {
                validLCR.cancel();
                validLocalChannelHashMap.remove(remoteId);
                logger.error("LocalChannelServer Interrupted: " + serverChannel.getClass().getName() + " " + serverChannel.getConfig().getConnectTimeoutMillis() + " " + serverChannel.isBound());
                throw new OpenR66ProtocolSystemException("Interruption - Cannot connect to local handler: " + socketLocalServerAddress + " " + serverChannel.isBound() + " " + serverChannel, e1);
            }
            if (channelFuture.isSuccess()) {
                final Channel channel = channelFuture.getChannel();
                localChannelGroup.add(channel);
                final LocalChannelReference localChannelReference = new LocalChannelReference(channel, networkChannel, remoteId, futureRequest);
                logger.debug("Create LocalChannel entry: " + i + " {}", localChannelReference);
                channel.getCloseFuture().addListener(remover);
                localChannelHashMap.put(channel.getId(), localChannelReference);
                try {
                    NetworkTransaction.addLocalChannelToNetworkChannel(networkChannel, channel);
                } catch (OpenR66ProtocolRemoteShutdownException e) {
                    validLCR.cancel();
                    validLocalChannelHashMap.remove(remoteId);
                    Channels.close(channel);
                    throw new OpenR66ProtocolSystemException("Cannot connect to local handler", e);
                }
                StartupPacket startup = new StartupPacket(localChannelReference.getLocalId());
                Channels.write(channel, startup).awaitUninterruptibly();
                validLCR.setSuccess();
                return localChannelReference;
            } else {
                logger.error("Can't connect to local server " + i);
            }
            try {
                Thread.sleep(Configuration.RETRYINMS);
            } catch (InterruptedException e) {
                validLCR.cancel();
                validLocalChannelHashMap.remove(remoteId);
                throw new OpenR66ProtocolSystemException("Cannot connect to local handler", e);
            }
        }
        validLCR.cancel();
        validLocalChannelHashMap.remove(remoteId);
        logger.error("LocalChannelServer: " + serverChannel.getClass().getName() + " " + serverChannel.getConfig().getConnectTimeoutMillis() + " " + serverChannel.isBound());
        throw new OpenR66ProtocolSystemException("Cannot connect to local handler: " + socketLocalServerAddress + " " + serverChannel.isBound() + " " + serverChannel, channelFuture.getCause());
    }
