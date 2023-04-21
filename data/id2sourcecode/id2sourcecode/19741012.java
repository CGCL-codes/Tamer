    public void announceWriteDemand(ManagedConnection connection) {
        SelectionKey key = connection.getChannel().keyFor(demultiplexer);
        if (key != null) {
            synchronized (dispatcherThreadGuard) {
                timeTrace("set readwrite op start");
                if (key.isValid()) {
                    key.selector().wakeup();
                    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
                timeTrace("set readwrite op end");
            }
        }
    }
