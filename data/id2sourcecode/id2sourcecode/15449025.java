    public final void close(final String clientId, final SocketChannel sChannel) {
        unregisterableClients.offer(new RegisterableClient(clientId, sChannel));
        keepAliveTimers.remove(clientId);
        totalMessageCount -= clientMessages.get(clientId).size();
        clientMessages.remove(clientId);
        if (writerThread != null) {
            writerThread.interrupt();
        }
        selector.wakeup();
    }
