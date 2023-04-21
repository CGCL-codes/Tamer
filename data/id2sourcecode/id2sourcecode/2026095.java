    public boolean deliver(Channel fromClient, AsyncMessage message) {
        if (noLocal && fromClient.getId().equals(channel.getId())) return false;
        if (selector == null || selector.accept(message)) {
            try {
                message.setHeader(AsyncMessage.DESTINATION_CLIENT_ID_HEADER, subscriptionId);
                getChannel().receive(message);
                return true;
            } catch (MessageReceivingException e) {
                log.error(e, "Could not deliver message");
            }
        }
        return false;
    }
