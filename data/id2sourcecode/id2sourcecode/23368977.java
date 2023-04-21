    private void registerHandlerNow(IoSocketHandler socketHandler, int ops) throws IOException {
        if (socketHandler.isOpen()) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("[" + socketHandler.getId() + "] registering connection");
            }
            try {
                socketHandler.getChannel().register(selector, ops, socketHandler);
                socketHandler.onRegisteredEvent();
                handledRegistractions++;
            } catch (Exception e) {
                socketHandler.close(e);
            }
        } else {
            socketHandler.onRegisteredFailedEvent(new IOException("could not register handler " + socketHandler.getId() + " because the channel is closed"));
        }
    }
