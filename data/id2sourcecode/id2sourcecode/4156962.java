    public void register(AbstractChannelBasedEndpoint handler) {
        assert (!handler.getChannel().isBlocking());
        boolean isWakeUpRequired = false;
        if (registerQueue.isEmpty()) {
            isWakeUpRequired = true;
        }
        registerQueue.add(handler);
        if (isWakeUpRequired) {
            wakeUp();
        } else {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("does not wake up selector, because register queue handling is currently running");
            }
        }
    }
