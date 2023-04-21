    public void setupContext(final String p_bizViewName, final Map<String, Object> p_params, final String p_inputXml) throws XAwareException {
        m_context = new BizDriverContext(getBizDriverIdentifier(), m_jdomStructure, p_params, p_inputXml);
        m_bizDriverType = m_jdomStructure.getRootElement().getAttributeValue(XAwareConstants.BIZDRIVER_ATTR_TYPE, XAwareConstants.xaNamespace);
        getChannelSpecification();
        getChannelPoolingSpecification();
    }
