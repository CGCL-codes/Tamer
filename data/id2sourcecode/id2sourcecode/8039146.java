    public Object createChannelObject() throws XAwareException {
        DataSource dataSource = (DataSource) this.m_channelSpecification.getChannelObject();
        this.m_channelPoolingSpecification.applyConfiguration(dataSource);
        return dataSource;
    }
