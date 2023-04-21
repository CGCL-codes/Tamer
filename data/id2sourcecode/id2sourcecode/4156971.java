    private int performRegisterHandlerTasks() throws IOException {
        int handledTasks = 0;
        while (true) {
            AbstractChannelBasedEndpoint handler = registerQueue.poll();
            if (handler == null) {
                return handledTasks;
            }
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("registering handler " + handler);
            }
            if (handler.getChannel().isOpen()) {
                handler.getChannel().register(selector, SelectionKey.OP_READ, handler);
                handledRegistractions++;
                handledTasks++;
            } else {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("channel " + handler.getId() + " is already closed. Could not register it");
                }
            }
        }
    }
