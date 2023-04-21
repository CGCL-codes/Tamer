    public final IChannelSpecification getChannelSpecification() throws XAwareConfigMissingException, XAwareConfigurationException, XAwareSubstitutionException, XAwareException {
        if (m_channelSpecification == null) {
            throw new XAwareConfigurationException("No configuration info provided for BizDriver " + getBizDriverIdentifier());
        }
        if (!m_channelSpecification.isSpecificationInfoTransferred()) {
            m_channelSpecification.transferSpecificationInfo(getBizDriverIdentifier(), getRootElement(), m_context);
        }
        return m_channelSpecification;
    }
