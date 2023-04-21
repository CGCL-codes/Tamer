    public void deregisterConnection(ManagedConnection connection) throws IOException {
        synchronized (dispatcherThreadGuard) {
            demultiplexer.wakeup();
            SelectionKey key = connection.getChannel().keyFor(demultiplexer);
            key.interestOps(0);
            key.cancel();
        }
    }
