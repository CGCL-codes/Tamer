    public FTPTemplate getTemplate(final FTPBizDriver bizDriver, IScriptNode node) throws XAwareException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        FTPTemplate template = (FTPTemplate) node.getChannelScope().getScopedChannel(IScopedChannel.Type.FTP, key);
        if (template == null) {
            template = new FTPTemplate(bizDriver);
            node.getChannelScope().setScopedChannel(key, template);
        }
        return template;
    }
