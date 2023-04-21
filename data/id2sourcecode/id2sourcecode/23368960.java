    private void deregisterAndCloseNow(IoSocketHandler socketHandler) {
        try {
            SelectionKey key = socketHandler.getChannel().keyFor(selector);
            if ((key != null) && key.isValid()) {
                key.cancel();
                if (roughNumOfRegisteredHandles > 0) {
                    roughNumOfRegisteredHandles--;
                }
            }
        } catch (Exception e) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("error occured by deregistering socket handler " + e.toString());
            }
        }
        socketHandler.onDeregisteredEvent();
    }
