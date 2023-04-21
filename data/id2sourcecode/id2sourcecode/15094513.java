    public SmtpTemplate getTemplate(final SmtpBizDriver bizDriver, IScriptNode node) throws XAwareException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        SmtpTemplate template = (SmtpTemplate) node.getChannelScope().getScopedChannel(IScopedChannel.Type.SMTP, key);
        if (template == null) {
            template = new SmtpTemplate(bizDriver);
            node.getChannelScope().setScopedChannel(key, template);
        }
        return template;
    }
