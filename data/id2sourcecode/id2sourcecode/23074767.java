    public Object createChannelObject() throws XAwareException {
        ContextSource contextSource = (ContextSource) this.m_channelSpecification.getChannelObject();
        this.m_channelPoolingSpecification.applyConfiguration(contextSource);
        return contextSource;
    }
