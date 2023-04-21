    void handleBridgeEvent(BridgeEvent event) {
        final AsteriskChannelImpl channel1 = getChannelImplById(event.getUniqueId1());
        final AsteriskChannelImpl channel2 = getChannelImplById(event.getUniqueId2());
        if (channel1 == null) {
            logger.error("Ignored BridgeEvent for unknown channel " + event.getChannel1());
            return;
        }
        if (channel2 == null) {
            logger.error("Ignored BridgeEvent for unknown channel " + event.getChannel2());
            return;
        }
        if (event.isLink()) {
            logger.info("Linking channels " + channel1.getName() + " and " + channel2.getName());
            synchronized (channel1) {
                channel1.channelLinked(event.getDateReceived(), channel2);
            }
            synchronized (channel2) {
                channel2.channelLinked(event.getDateReceived(), channel1);
            }
        }
        if (event.isUnlink()) {
            logger.info("Unlinking channels " + channel1.getName() + " and " + channel2.getName());
            synchronized (channel1) {
                channel1.channelUnlinked(event.getDateReceived());
            }
            synchronized (channel2) {
                channel2.channelUnlinked(event.getDateReceived());
            }
        }
    }
