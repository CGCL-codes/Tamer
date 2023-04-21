    void handleStatusEvent(StatusEvent event) {
        AsteriskChannelImpl channel;
        final Extension extension;
        boolean isNew = false;
        Map<String, String> variables = event.getVariables();
        channel = getChannelImplById(event.getUniqueId());
        if (channel == null) {
            Date dateOfCreation;
            if (event.getSeconds() != null) {
                dateOfCreation = new Date(DateUtil.getDate().getTime() - (event.getSeconds() * 1000L));
            } else {
                dateOfCreation = DateUtil.getDate();
            }
            channel = new AsteriskChannelImpl(server, event.getChannel(), event.getUniqueId(), dateOfCreation);
            isNew = true;
            if (variables != null) {
                for (String variable : variables.keySet()) {
                    channel.updateVariable(variable, variables.get(variable));
                }
            }
        }
        if (event.getContext() == null && event.getExtension() == null && event.getPriority() == null) {
            extension = null;
        } else {
            extension = new Extension(event.getContext(), event.getExtension(), event.getPriority());
        }
        synchronized (channel) {
            channel.setCallerId(new CallerId(event.getCallerIdName(), event.getCallerIdNum()));
            channel.setAccount(event.getAccountCode());
            if (event.getChannelState() != null) {
                channel.stateChanged(event.getDateReceived(), ChannelState.valueOf(event.getChannelState()));
            }
            channel.extensionVisited(event.getDateReceived(), extension);
            if (event.getBridgedChannel() != null) {
                final AsteriskChannelImpl linkedChannel = getChannelImplByName(event.getBridgedChannel());
                if (linkedChannel != null) {
                    channel.channelLinked(event.getDateReceived(), linkedChannel);
                    synchronized (linkedChannel) {
                        linkedChannel.channelLinked(event.getDateReceived(), channel);
                    }
                }
            }
        }
        if (isNew) {
            logger.info("Adding new channel " + channel.getName());
            addChannel(channel);
            server.fireNewAsteriskChannel(channel);
        }
    }
