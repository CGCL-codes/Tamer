    @Override
    public void publish(PubSubMessage message) throws MessagingException {
        message.setFrom(getFullJid());
        PubSub pubsub = createPubSub();
        pubsub.setType(Type.SET);
        String buffer = encode(message);
        PublishElement publishElement = new PublishElement(prepareChannelName(message.getChannelName()));
        publishElement.addChild(new ItemElement(null, "<" + ENTRY + ">" + buffer + "</" + ENTRY + ">"));
        pubsub.addChild(publishElement);
        connection.sendPacket(pubsub);
    }
