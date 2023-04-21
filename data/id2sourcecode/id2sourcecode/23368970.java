    private SelectionKey getSelectionKey(IoSocketHandler socketHandler) {
        SelectionKey key = socketHandler.getChannel().keyFor(selector);
        if (LOG.isLoggable(Level.FINE)) {
            if (key == null) {
                LOG.fine("[" + socketHandler.getId() + "] key is null");
            } else if (!key.isValid()) {
                LOG.fine("[" + socketHandler.getId() + "] key is not valid");
            }
        }
        return key;
    }
