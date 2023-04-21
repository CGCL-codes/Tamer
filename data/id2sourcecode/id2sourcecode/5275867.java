    protected ContentManager.LocalChannelDefinition<ProxyChannel> createProxyChannelDefinitionFromEntry(final FeedPageDescriptor feedPageDescriptor, final SyndFeed syndFeed, final ContentManager.ChannelGroupDefinition channelGroupDefinition, final SyndEntry syndEntry, final int entryIndex) throws WWWeeePortal.Exception {
        final String channelID = createID(syndEntry.getUri(), "entry_" + String.valueOf(entryIndex));
        final ContentManager.LocalChannelSpecification<ProxyChannel> localChannelSpecification = new ContentManager.LocalChannelSpecification<ProxyChannel>(channelGroupDefinition, new RSProperties(this, channelGroupDefinition.getProperties()), channelID, null);
        final RSProperties channelProperties = new RSProperties(this, localChannelSpecification.getProperties());
        if (syndEntry.getTitle() != null) channelProperties.setProp(Channel.TITLE_TEXT_PROP, syndEntry.getTitle(), Locale.ROOT, null);
        final String link = syndEntry.getLink();
        channelProperties.setProp(ProxyChannel.BASE_URI_PROP, link, Locale.ROOT, null);
        final ContentManager.LocalChannelDefinition<ProxyChannel> channelDefinition = new ContentManager.LocalChannelDefinition<ProxyChannel>(localChannelSpecification, channelProperties, feedPageDescriptor.getChannelAccessControl(), ProxyChannel.class);
        new ContentManager.ChannelPluginDefinition<ProxyChannelHTMLSource>(channelDefinition, new RSProperties(this, channelDefinition.getProperties()), null, ProxyChannelHTMLSource.class);
        new ContentManager.ChannelPluginDefinition<ChannelTitle>(channelDefinition, new RSProperties(this, channelDefinition.getProperties()), null, ChannelTitle.class);
        new ContentManager.ChannelPluginDefinition<ChannelCache>(channelDefinition, new RSProperties(this, channelDefinition.getProperties()), null, ChannelCache.class);
        return channelDefinition;
    }
