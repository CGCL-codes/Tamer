    public AsteriskChannel originate(OriginateAction originateAction) throws ManagerCommunicationException, NoSuchChannelException {
        final ResponseEvents responseEvents;
        final Iterator<ResponseEvent> responseEventIterator;
        String uniqueId;
        AsteriskChannel channel = null;
        originateAction.setAsync(Boolean.TRUE);
        initializeIfNeeded();
        responseEvents = sendEventGeneratingAction(originateAction, originateAction.getTimeout() + 2000);
        responseEventIterator = responseEvents.getEvents().iterator();
        if (responseEventIterator.hasNext()) {
            ResponseEvent responseEvent;
            responseEvent = responseEventIterator.next();
            if (responseEvent instanceof OriginateResponseEvent) {
                uniqueId = ((OriginateResponseEvent) responseEvent).getUniqueId();
                logger.debug(responseEvent.getClass().getName() + " received with uniqueId " + uniqueId);
                channel = getChannelById(uniqueId);
            }
        }
        if (channel == null) {
            throw new NoSuchChannelException("Channel '" + originateAction.getChannel() + "' is not available");
        }
        return channel;
    }
