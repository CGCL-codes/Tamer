    void handleUnparkedCallEvent(UnparkedCallEvent event) {
        final AsteriskChannelImpl channel = getChannelImplByNameAndActive(event.getChannel());
        if (channel == null) {
            logger.info("Ignored UnparkedCallEvent for unknown channel " + event.getChannel());
            return;
        }
        Extension wasParkedAt = channel.getParkedAt();
        if (wasParkedAt == null) {
            logger.info("Ignored UnparkedCallEvent as the channel was not parked");
            return;
        }
        synchronized (channel) {
            channel.setParkedAt(null);
        }
        logger.info("Channel " + channel.getName() + " is unparked (moved away) from " + wasParkedAt.getExtension());
    }
