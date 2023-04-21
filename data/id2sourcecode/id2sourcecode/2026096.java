    public Message getUnsubscribeMessage() {
        CommandMessage unsubscribeMessage = new CommandMessage();
        unsubscribeMessage.setOperation(CommandMessage.UNSUBSCRIBE_OPERATION);
        unsubscribeMessage.setClientId(getChannel().getId());
        unsubscribeMessage.setDestination(destination);
        unsubscribeMessage.setHeader(AsyncMessage.SUBTOPIC_HEADER, getSubTopicId());
        unsubscribeMessage.setHeader(AsyncMessage.DESTINATION_CLIENT_ID_HEADER, getSubscriptionId());
        return unsubscribeMessage;
    }
