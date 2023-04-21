    public void registerConnection(ManagedConnection connection) throws IOException {
        connection.setIOMemoryManager(ioMemoryManager);
        connection.setConnectionListener(eventHandler);
        synchronized (dispatcherThreadGuard) {
            demultiplexer.wakeup();
            connection.getChannel().register(demultiplexer, SelectionKey.OP_READ, connection);
        }
        eventHandler.onDispatcherRegisteredEvent(connection);
        handledConnections++;
    }
