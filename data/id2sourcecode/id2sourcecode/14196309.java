    public JMXTemplate getJMXTemplate(IBizDriver bizDriver, IScriptNode node) throws XAwareException {
        final IChannelKey key = bizDriver.getChannelKey();
        JMXTemplate template = (JMXTemplate) node.getChannelScope().getScopedChannel(IScopedChannel.Type.JMX, key);
        if (template == null) {
            template = (JMXTemplate) bizDriver.createTemplate();
            node.getChannelScope().setScopedChannel(key, template);
        }
        return template;
    }
