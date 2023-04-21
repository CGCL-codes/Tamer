    void handleDialEvent(DialEvent event) {
        final AsteriskChannelImpl sourceChannel = getChannelImplById(event.getUniqueId());
        final AsteriskChannelImpl destinationChannel = getChannelImplById(event.getDestUniqueId());
        if (sourceChannel == null) {
            logger.error("Ignored DialEvent for unknown source channel " + event.getChannel() + " with unique id " + event.getUniqueId());
            return;
        }
        if (destinationChannel == null) {
            logger.error("Ignored DialEvent for unknown destination channel " + event.getDestination() + " with unique id " + event.getDestUniqueId());
            return;
        }
        logger.info(sourceChannel.getName() + " dialed " + destinationChannel.getName());
        getTraceId(sourceChannel);
        getTraceId(destinationChannel);
        synchronized (sourceChannel) {
            sourceChannel.channelDialed(event.getDateReceived(), destinationChannel);
        }
        synchronized (destinationChannel) {
            destinationChannel.channelDialing(event.getDateReceived(), sourceChannel);
        }
    }
