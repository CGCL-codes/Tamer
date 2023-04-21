    public void inspectChannel(Message message, Destination destination) {
        if (!enforceEndpointValidation && message.getHeader(Message.VALIDATE_ENDPOINT_HEADER) == null) return;
        String messageChannel = (String) message.getHeader(Message.ENDPOINT_HEADER);
        for (String channelId : destination.getChannels()) {
            if (channelId.equals(messageChannel)) return;
        }
        MessageException lme = new MessageException();
        lme.setMessage(ERR_MSG_DESTINATION_UNACCESSIBLE, new Object[] { destination.getId(), messageChannel });
        throw lme;
    }
