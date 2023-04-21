    void handleHangupEvent(HangupEvent event) {
        HangupCause cause = null;
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
        if (channel == null) {
            logger.error("Ignored HangupEvent for unknown channel " + event.getChannel());
            return;
        }
        if (event.getCause() != null) {
            cause = HangupCause.getByCode(event.getCause());
        }
        synchronized (channel) {
            channel.hungup(event.getDateReceived(), cause, event.getCauseTxt());
        }
        logger.info("Removing channel " + channel.getName() + " due to hangup (" + cause + ")");
        removeOldChannels();
    }
