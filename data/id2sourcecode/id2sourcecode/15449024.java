    public final void create(final String clientId, final SocketChannel sChannel) {
        registerableClients.offer(new RegisterableClient(clientId, sChannel));
        clientMessages.put(clientId, new ArrayBlockingQueue<Message>(QUEUE_SIZE));
        keepAliveTimers.put(clientId, 0L);
        if (writerThread != null) {
            writerThread.interrupt();
        }
        selector.wakeup();
    }
