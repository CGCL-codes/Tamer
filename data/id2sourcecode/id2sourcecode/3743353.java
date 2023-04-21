    public ISalesForceTemplate getTemplate(final IBizDriver bizDriver, IScriptNode node) throws XAwareException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        ISalesForceTemplate template = null;
        if (node != null) {
            template = (SalesForceTemplate) node.getChannelScope().getScopedChannel(IScopedChannel.Type.SF, key);
        }
        if (template == null) {
            template = (ISalesForceTemplate) bizDriver.createTemplate();
            if (node != null) {
                node.getChannelScope().setScopedChannel(key, template);
            }
        }
        return template;
    }
