    public boolean register(IoSocketHandler socketHandler, int ops) throws IOException {
        assert (!socketHandler.getChannel().isBlocking());
        socketHandler.setMemoryManager(memoryManager);
        if (isDispatcherInstanceThread()) {
            registerHandlerNow(socketHandler, ops);
        } else {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("[" + socketHandler.getId() + "] add new connection to register task queue");
            }
            registerQueue.add(new RegisterTask(socketHandler, ops));
            wakeUp();
        }
        return true;
    }
