    public final IChannelPoolingSpecification getChannelPoolingSpecification() throws XAwareConfigMissingException, XAwareConfigurationException, XAwareSubstitutionException, XAwareException {
        if (m_channelPoolingSpecification != null && !m_channelPoolingSpecification.isSpecificationInfoTransferred()) {
            m_channelPoolingSpecification.transferSpecificationInfo(getBizDriverIdentifier(), getRootElement(), m_context);
        }
        return m_channelPoolingSpecification;
    }
