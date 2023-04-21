    private AsteriskChannelImpl addNewChannel(String uniqueId, String name, Date dateOfCreation, String callerIdNumber, String callerIdName, ChannelState state, String account) {
        final AsteriskChannelImpl channel;
        final String traceId;
        channel = new AsteriskChannelImpl(server, name, uniqueId, dateOfCreation);
        channel.setCallerId(new CallerId(callerIdName, callerIdNumber));
        channel.setAccount(account);
        channel.stateChanged(dateOfCreation, state);
        logger.info("Adding channel " + channel.getName() + "(" + channel.getId() + ")");
        if (SLEEP_TIME_BEFORE_GET_VAR > 0) {
            try {
                Thread.sleep(SLEEP_TIME_BEFORE_GET_VAR);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        traceId = getTraceId(channel);
        channel.setTraceId(traceId);
        addChannel(channel);
        if (traceId != null && (!name.toLowerCase(Locale.ENGLISH).startsWith("local/") || (name.endsWith(",1") || name.endsWith(";1")))) {
            final OriginateCallbackData callbackData;
            callbackData = server.getOriginateCallbackDataByTraceId(traceId);
            if (callbackData != null && callbackData.getChannel() == null) {
                callbackData.setChannel(channel);
                try {
                    callbackData.getCallback().onDialing(channel);
                } catch (Throwable t) {
                    logger.warn("Exception dispatching originate progress.", t);
                }
            }
        }
        server.fireNewAsteriskChannel(channel);
        return channel;
    }
