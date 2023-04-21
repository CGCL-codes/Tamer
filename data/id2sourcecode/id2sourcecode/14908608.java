    public static Channel findChannelInParentTags(String channelName, Tag tag) throws JellyException {
        PanelTag panelTag = (PanelTag) TagSupport.findAncestorWithClass(tag, PanelTag.class);
        if (panelTag == null) {
            throw new JellyException("No enclosing tag supplies channel '" + channelName + "'");
        }
        ChannelProvider provider = (ChannelProvider) panelTag.getComponent();
        try {
            return provider.getChannel(channelName);
        } catch (IllegalArgumentException e) {
        }
        return findChannelInParentTags(channelName, tag.getParent());
    }
