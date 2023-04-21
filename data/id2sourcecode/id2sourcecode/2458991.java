    public void deregister(final T handle) throws IOException {
        synchronized (dispatcherThreadGuard) {
            selector.wakeup();
            SelectionKey key = handle.getChannel().keyFor(selector);
            if (key.isValid()) {
                key.cancel();
            }
        }
    }
