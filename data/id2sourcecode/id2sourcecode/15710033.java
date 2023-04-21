    void handleDtmfEvent(DtmfEvent event) {
        if (event.isBegin()) {
            return;
        }
        if (event.getUniqueId() == null) {
            return;
        }
        final AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
        if (channel == null) {
            logger.info("Ignored DtmfEvent for unknown channel with uniqueId " + event.getUniqueId());
            return;
        }
        final Character dtmfDigit;
        if (event.getDigit() == null || event.getDigit().length() < 1) {
            dtmfDigit = null;
        } else {
            dtmfDigit = event.getDigit().charAt(0);
        }
        synchronized (channel) {
            if (event.isReceived()) {
                channel.dtmfReceived(dtmfDigit);
            }
            if (event.isSent()) {
                channel.dtmfSent(dtmfDigit);
            }
        }
    }
