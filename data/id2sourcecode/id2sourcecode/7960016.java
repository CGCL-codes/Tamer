    public void sendMessage(Message message, String channelId) throws MessageException {
        Channel channel = channelManager.getChannel(channelId);
        if (channel == null) {
            throw new MessageException("Channnel '" + channelId + "' not found.");
        }
        MessageProducer messageProducer = messageProducers.get(channelId);
        if (messageProducer == null) {
            messageProducer = messageProducerFactory.createMessageProducer();
            messageProducers.put(channelId, messageProducer);
        }
        message.setDestination(channelId);
        messageProducer.produceMessage(message);
    }
