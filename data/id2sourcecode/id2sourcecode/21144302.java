    @Override
    public void publish(PubSubMessage message) throws MessagingException {
        try {
            Topic topic = session.createTopic(prepareChannelName(message.getChannelName()));
            TopicPublisher publisher = session.createPublisher(topic);
            publishers.add(publisher);
            ObjectMessage jmsMessage = session.createObjectMessage();
            jmsMessage.setObject(message);
            publisher.publish(topic, jmsMessage);
        } catch (JMSException e) {
            throw new MessagingException(e);
        }
    }
