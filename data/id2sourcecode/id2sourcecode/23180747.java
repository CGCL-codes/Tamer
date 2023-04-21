    @Override
    public final void disconnect(final InSimHost inSimHost) {
        final ConnectionInfo connInfo = fConnectionsInfo.remove(inSimHost);
        final SelectableChannel channel = connInfo.getChannel();
        if (channel != null) {
            synchronized (fChangeRequests) {
                fChangeRequests.add(new ChangeRequest(connInfo, ChangeRequestType.CANCEL, 0));
            }
        }
        fSelector.wakeup();
        logger.info(AbstractInSimNioClient.FORMAT_UTILS.format(LogMessages.getString("Client.disconnected"), inSimHost));
    }
