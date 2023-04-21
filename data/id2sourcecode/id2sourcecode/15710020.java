    void handleNewStateEvent(NewStateEvent event) {
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
        if (channel == null) {
            channel = getChannelImplByNameAndActive(event.getChannel());
            if (channel != null) {
                logger.info("Changing unique id for '" + channel.getName() + "' from " + channel.getId() + " to " + event.getUniqueId());
                channel.idChanged(event.getDateReceived(), event.getUniqueId());
            }
            if (channel == null) {
                logger.info("Creating new channel due to NewStateEvent '" + event.getChannel() + "' unique id " + event.getUniqueId());
                channel = addNewChannel(event.getUniqueId(), event.getChannel(), event.getDateReceived(), event.getCallerIdNum(), event.getCallerIdName(), ChannelState.valueOf(event.getChannelState()), null);
            }
        }
        if (event.getCallerIdNum() != null || event.getCallerIdName() != null) {
            String cidnum = "";
            String cidname = "";
            CallerId currentCallerId = channel.getCallerId();
            if (currentCallerId != null) {
                cidnum = currentCallerId.getNumber();
                cidname = currentCallerId.getName();
            }
            if (event.getCallerIdNum() != null) {
                cidnum = event.getCallerIdNum();
            }
            if (event.getCallerIdName() != null) {
                cidname = event.getCallerIdName();
            }
            CallerId newCallerId = new CallerId(cidname, cidnum);
            logger.debug("Updating CallerId (following NewStateEvent) to: " + newCallerId.toString());
            channel.setCallerId(newCallerId);
            if (event.getChannel() != null && !event.getChannel().equals(channel.getName())) {
                logger.info("Renaming channel (following NewStateEvent) '" + channel.getName() + "' to '" + event.getChannel() + "'");
                synchronized (channel) {
                    channel.nameChanged(event.getDateReceived(), event.getChannel());
                }
            }
        }
        if (event.getChannelState() != null) {
            synchronized (channel) {
                channel.stateChanged(event.getDateReceived(), ChannelState.valueOf(event.getChannelState()));
            }
        }
    }
