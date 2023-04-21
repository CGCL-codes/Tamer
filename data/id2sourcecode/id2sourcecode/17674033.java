    public FileTemplate getTemplate(final FileBizDriver bizDriver, IScriptNode node) throws XAwareException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        FileTemplate template = (FileTemplate) node.getChannelScope().getScopedChannel(IScopedChannel.Type.FILE, key);
        if (template == null) {
            template = new FileTemplate(bizDriver);
            node.getChannelScope().setScopedChannel(key, template);
        }
        return template;
    }
