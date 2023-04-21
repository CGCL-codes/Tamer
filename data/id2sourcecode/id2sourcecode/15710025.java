    void handleRenameEvent(RenameEvent event) {
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
        if (channel == null) {
            logger.error("Ignored RenameEvent for unknown channel with uniqueId " + event.getUniqueId());
            return;
        }
        logger.info("Renaming channel '" + channel.getName() + "' to '" + event.getNewname() + "', uniqueId is " + event.getUniqueId());
        synchronized (channel) {
            channel.nameChanged(event.getDateReceived(), event.getNewname());
        }
    }
