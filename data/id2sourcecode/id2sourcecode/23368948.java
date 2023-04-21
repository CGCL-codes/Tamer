    @Override
    void reinit() throws IOException {
        Selector oldSelector = selector;
        HashSet<SelectionKey> keys = new HashSet<SelectionKey>();
        keys.addAll(oldSelector.keys());
        selector = Selector.open();
        for (SelectionKey key : keys) {
            int ops = key.interestOps();
            IoSocketHandler socketHandler = (IoSocketHandler) key.attachment();
            key.cancel();
            try {
                socketHandler.getChannel().register(selector, ops, socketHandler);
            } catch (IOException ioe) {
                LOG.warning("could not reinit " + socketHandler.toString() + " " + DataConverter.toString(ioe));
            }
        }
        oldSelector.close();
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("selector has been reinitialized");
        }
    }
