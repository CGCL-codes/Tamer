    void handleNewCallerIdEvent(NewCallerIdEvent event) {
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
        if (channel == null) {
            channel = getChannelImplByNameAndActive(event.getChannel());
            if (channel != null) {
                logger.info("Changing unique id for '" + channel.getName() + "' from " + channel.getId() + " to " + event.getUniqueId());
                channel.idChanged(event.getDateReceived(), event.getUniqueId());
            }
            if (channel == null) {
                channel = addNewChannel(event.getUniqueId(), event.getChannel(), event.getDateReceived(), event.getCallerIdNum(), event.getCallerIdName(), ChannelState.DOWN, null);
            }
        }
        synchronized (channel) {
            channel.setCallerId(new CallerId(event.getCallerIdName(), event.getCallerIdNum()));
        }
    }
