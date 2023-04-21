    public Object createChannelObject() throws XAwareException {
        Object connectionFactory = this.m_channelSpecification.getChannelObject();
        this.m_channelPoolingSpecification.applyConfiguration(connectionFactory);
        return connectionFactory;
    }
