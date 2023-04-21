    @Override
    public final void send(final InetSocketAddress hostAddress, final byte[] data) {
        final ConnectionInfo connInfo = fConnectionsInfo.get(hostAddress);
        final SelectableChannel channel = connInfo.getChannel();
        if (channel == null || !this.isConnected(hostAddress)) {
            throw new IllegalStateException(fFormatUtils.format(ExceptionMessages.getString("InSimClient.ise.connectionNotEstablished"), hostAddress != null ? hostAddress : "null"));
        }
        synchronized (fPendingRequestData) {
            List<ByteBuffer> sendQueue = fPendingRequestData.get(channel);
            if (sendQueue == null) {
                sendQueue = new ArrayList<ByteBuffer>();
                fPendingRequestData.put(channel, sendQueue);
            }
            final ByteBuffer buf = ByteBuffer.wrap(data);
            buf.rewind();
            sendQueue.add(buf);
        }
        synchronized (fChangeRequests) {
            fChangeRequests.add(fChangeRequestFactory.createChangeRequest(connInfo, ChangeRequestType.CHANGE_OPS, SelectionKey.OP_WRITE));
        }
        fSelector.wakeup();
    }
