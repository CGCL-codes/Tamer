    public void setupContext(final IBizViewContext p_parentContext, final BizDriverConfig p_bizDriverConfig) throws XAwareException {
        m_bizDriverIdentifier = p_bizDriverConfig.getBizDriverName();
        if (p_parentContext == null) {
            throw new XAwareException("parent context is null");
        }
        m_context = new BizDriverContext(getBizDriverIdentifier(), p_parentContext, m_jdomStructure, p_bizDriverConfig.getParams());
        m_bizDriverType = m_jdomStructure.getRootElement().getAttributeValue(XAwareConstants.BIZDRIVER_ATTR_TYPE, XAwareConstants.xaNamespace);
        m_context.setInputXmlRoot(p_bizDriverConfig.getInputXML());
        getChannelSpecification();
        getChannelPoolingSpecification();
    }
