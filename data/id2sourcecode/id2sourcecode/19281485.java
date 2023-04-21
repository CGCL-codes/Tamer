    public IGenericChannelTemplate getChannelTemplate() throws XAwareException {
        return new JMXTemplate(m_props);
    }
