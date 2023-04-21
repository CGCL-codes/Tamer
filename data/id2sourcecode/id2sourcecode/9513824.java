    @Override
    public final void disconnect(final InetSocketAddress hostAddress) {
        final ConnectionInfo connInfo = fConnectionsInfo.remove(hostAddress);
        final SelectableChannel channel = connInfo.getChannel();
        if (channel != null) {
            synchronized (fChangeRequests) {
                fChangeRequests.add(fChangeRequestFactory.createChangeRequest(connInfo, ChangeRequestType.CANCEL, 0));
            }
        }
        fSelector.wakeup();
        logger.info(fFormatUtils.format(LogMessages.getString("Client.disconnected"), hostAddress));
    }
