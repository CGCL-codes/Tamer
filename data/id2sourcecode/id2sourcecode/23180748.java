    @Override
    public final void send(final InSimHost inSimHost, final InSimRequestPacket requestPacket) {
        final ConnectionInfo connInfo = fConnectionsInfo.get(inSimHost);
        final SelectableChannel channel = connInfo.getChannel();
        if ((channel == null) || !this.isConnected(inSimHost)) {
            throw new IllegalStateException(AbstractInSimNioClient.FORMAT_UTILS.format(ExceptionMessages.getString("InSimClient.ise.connectionNotEstablished"), inSimHost != null ? inSimHost : "null"));
        }
        synchronized (fPendingRequestData) {
            List<ByteBuffer> queue = fPendingRequestData.get(channel);
            if (queue == null) {
                queue = new ArrayList<ByteBuffer>();
                fPendingRequestData.put(channel, queue);
            }
            final ByteBuffer buf = requestPacket.compile();
            buf.rewind();
            queue.add(buf);
        }
        synchronized (fChangeRequests) {
            fChangeRequests.add(new ChangeRequest(connInfo, ChangeRequestType.CHANGE_OPS, SelectionKey.OP_WRITE));
        }
        logger.debug(AbstractInSimNioClient.FORMAT_UTILS.format(LogMessages.getString("Client.send.packetQueued"), inSimHost, requestPacket));
        fSelector.wakeup();
    }
