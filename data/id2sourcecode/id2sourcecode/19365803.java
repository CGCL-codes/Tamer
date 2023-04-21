    @Override
    public void publish(PubSubMessage message) throws MessagingException {
        message.setFrom(id);
        if (channels.containsKey(message.getChannelName())) {
            channels.get(message.getChannelName()).publish(message);
        } else {
            log.info("No channel named " + message.getChannelName());
        }
    }
