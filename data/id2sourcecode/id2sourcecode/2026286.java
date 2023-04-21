    public HttpTemplate getTemplate(final HttpBizDriver bizDriver, IScriptNode node) throws XAwareException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        HttpTemplate template = (HttpTemplate) node.getChannelScope().getScopedChannel(IScopedChannel.Type.HTTP, key);
        if (template == null) {
            template = new HttpTemplate(bizDriver);
            node.getChannelScope().setScopedChannel(key, template);
        }
        return template;
    }
