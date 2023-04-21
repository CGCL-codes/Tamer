    void handleCdrEvent(CdrEvent event) {
        final AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
        final AsteriskChannelImpl destinationChannel = getChannelImplByName(event.getDestinationChannel());
        final CallDetailRecordImpl cdr;
        if (channel == null) {
            logger.info("Ignored CdrEvent for unknown channel with uniqueId " + event.getUniqueId());
            return;
        }
        cdr = new CallDetailRecordImpl(channel, destinationChannel, event);
        synchronized (channel) {
            channel.callDetailRecordReceived(event.getDateReceived(), cdr);
        }
    }
