    public Object createChannelObject() throws XAwareException {
        m_channelSpecification.transferSpecificationInfo(m_bizDriverIdentifier, m_jdomStructure.getRootElement(), m_context);
        return m_channelSpecification.getChannelObject();
    }
