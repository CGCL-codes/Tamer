    public void register(T handle, int ops) throws IOException {
        assert (!handle.getChannel().isBlocking());
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("register handle " + handle);
        }
        synchronized (dispatcherThreadGuard) {
            selector.wakeup();
            handle.getChannel().register(selector, ops, handle);
            eventHandler.onHandleRegisterEvent(handle);
        }
        handledRegistractions++;
    }
