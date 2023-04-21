    void handleParkedCallTimeOutEvent(ParkedCallTimeOutEvent event) {
        final AsteriskChannelImpl channel = getChannelImplByNameAndActive(event.getChannel());
        if (channel == null) {
            logger.info("Ignored ParkedCallTimeOutEvent for unknown channel " + event.getChannel());
            return;
        }
        Extension wasParkedAt = channel.getParkedAt();
        if (wasParkedAt == null) {
            logger.info("Ignored ParkedCallTimeOutEvent as the channel was not parked");
            return;
        }
        synchronized (channel) {
            channel.setParkedAt(null);
        }
        logger.info("Channel " + channel.getName() + " is unparked (Timeout) from " + wasParkedAt.getExtension());
    }
