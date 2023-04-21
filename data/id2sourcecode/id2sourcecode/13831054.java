    public FileTemplate54 getTemplate(final FileBizDriver bizDriver, IScriptNode node) throws XAwareException {
        final IChannelKey key = bizDriver.getChannelKey();
        FileTemplate54 template = (FileTemplate54) node.getChannelScope().getScopedChannel(IScopedChannel.Type.FILE, key);
        if (template == null) {
            template = new FileTemplate54(bizDriver);
            node.getChannelScope().setScopedChannel(key, template);
        }
        return template;
    }
