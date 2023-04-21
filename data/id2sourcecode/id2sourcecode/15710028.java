    void handleParkedCallEvent(ParkedCallEvent event) {
        AsteriskChannelImpl channel = getChannelImplByNameAndActive(event.getChannel());
        if (channel == null) {
            logger.info("Ignored ParkedCallEvent for unknown channel " + event.getChannel());
            return;
        }
        synchronized (channel) {
            Extension ext = new Extension(null, event.getExten(), 1);
            channel.setParkedAt(ext);
            logger.info("Channel " + channel.getName() + " is parked at " + channel.getParkedAt().getExtension());
        }
    }
