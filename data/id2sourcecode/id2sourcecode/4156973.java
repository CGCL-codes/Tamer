    private int performDeregisterHandlerTasks() {
        int handledTasks = 0;
        while (true) {
            AbstractChannelBasedEndpoint handler = deregisterQueue.poll();
            if (handler == null) {
                return handledTasks;
            }
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("registering handler " + handler);
            }
            SelectionKey key = handler.getChannel().keyFor(selector);
            if ((key != null) && key.isValid()) {
                key.cancel();
            }
            handledTasks++;
        }
    }
