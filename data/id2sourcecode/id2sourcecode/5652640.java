    public void enqueue(Message message, Channel channel) {
        try {
            _bootstrapJMS.enqueue(_bootstrapJMS.getJmsDestination(channel.getChannelName()), message.getBody());
        } catch (NamingException e) {
            throw new RuntimeException("Couldn't find queue named [" + channel.getChannelName() + "].", e);
        } catch (JMSException e) {
            throw new RuntimeException();
        }
    }
