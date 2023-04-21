    void handleVarSetEvent(VarSetEvent event) {
        if (event.getUniqueId() == null) {
            return;
        }
        final AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
        if (channel == null) {
            logger.info("Ignored VarSetEvent for unknown channel with uniqueId " + event.getUniqueId());
            return;
        }
        synchronized (channel) {
            channel.updateVariable(event.getVariable(), event.getValue());
        }
    }
