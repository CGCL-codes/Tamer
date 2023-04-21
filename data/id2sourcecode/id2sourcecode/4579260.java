    public SoapTemplate getTemplate(final SoapBizDriver bizDriver, IScriptNode node) throws XAwareException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        SoapTemplate template = (SoapTemplate) node.getChannelScope().getScopedChannel(IScopedChannel.Type.SOAP, key);
        if (template == null) {
            template = (SoapTemplate) bizDriver.createChannelObject();
            node.getChannelScope().setScopedChannel(key, template);
        }
        return template;
    }
