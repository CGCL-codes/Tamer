    public final void updateInterestSet(T handle, int ops) throws IOException {
        SelectionKey key = handle.getChannel().keyFor(selector);
        if (key != null) {
            synchronized (dispatcherThreadGuard) {
                if (key.isValid()) {
                    key.selector().wakeup();
                    if (LOG.isLoggable(Level.FINER)) {
                        LOG.finer("updating interest ops for " + handle + ". current value is " + printSelectionKeyValue(key.interestOps()));
                    }
                    key.interestOps(ops);
                    if (LOG.isLoggable(Level.FINER)) {
                        LOG.finer("interest ops has been updated to " + printSelectionKeyValue(ops));
                    }
                } else {
                    throw new IOException("handle " + handle + " is invalid ");
                }
            }
        }
    }
