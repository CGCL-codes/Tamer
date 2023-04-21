    @Override
    public void publish(PubSubMessage message) throws MessagingException {
        try {
            amqpChannel.basicPublish(message.getChannelName(), "#", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getPayload().getBytes());
        } catch (IOException e) {
            throw new MessagingException(e);
        }
    }
