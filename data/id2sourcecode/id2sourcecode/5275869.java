    @SuppressWarnings("unchecked")
    protected ContentManager.LocalChannelDefinition<StaticChannel> createStaticChannelDefinitionFromEntry(final FeedPageDescriptor feedPageDescriptor, final SyndFeed syndFeed, final ContentManager.ChannelGroupDefinition channelGroupDefinition, final SyndEntry syndEntry, final int entryIndex) throws WWWeeePortal.Exception {
        final String channelID = createID(syndEntry.getUri(), "entry_" + String.valueOf(entryIndex));
        final ContentManager.LocalChannelSpecification<StaticChannel> localChannelSpecification = new ContentManager.LocalChannelSpecification<StaticChannel>(channelGroupDefinition, new RSProperties(this, channelGroupDefinition.getProperties()), channelID, null);
        final RSProperties channelProperties = new RSProperties(this, localChannelSpecification.getProperties());
        if (syndEntry.getTitle() != null) channelProperties.setProp(Channel.TITLE_TEXT_PROP, syndEntry.getTitle(), Locale.ROOT, null);
        if (syndEntry.getUri() != null) channelProperties.setProp("Feed.Entry.URI", syndEntry.getUri(), Locale.ROOT, null);
        if (syndEntry.getAuthor() != null) channelProperties.setProp("Feed.Entry.Author", syndEntry.getAuthor(), Locale.ROOT, null);
        if (syndEntry.getPublishedDate() != null) channelProperties.setProp("Feed.Entry.PublishedDate", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, ConversionUtil.invokeConverter(DateUtil.DATE_UTC_CALENDAR_CONVERTER, syndEntry.getPublishedDate())), Locale.ROOT, null);
        final String alink = syndEntry.getLink();
        if (alink != null) {
            channelProperties.setProp("Feed.Entry.Link", alink, Locale.ROOT, null);
            if (!disableFeedTitleLinks) channelProperties.setProp(ChannelTitle.TITLE_ANCHOR_HREF_PROP, alink, Locale.ROOT, null);
        }
        final List<SyndLink> links = syndEntry.getLinks();
        if (links != null) {
            for (SyndLink link : links) {
                final String name = "Feed.Entry.Link" + ((link.getRel() != null) ? '.' + link.getRel() : "");
                channelProperties.setProp(name, link.getHref(), Locale.ROOT, null);
                if ((!disableFeedTitleLinks) && ("alternate".equalsIgnoreCase(link.getRel()))) {
                    channelProperties.setProp(ChannelTitle.TITLE_ANCHOR_HREF_PROP, link.getHref(), Locale.ROOT, null);
                    channelProperties.setProp(ChannelTitle.TITLE_ANCHOR_REL_PROP, link.getRel(), Locale.ROOT, null);
                }
            }
        }
        Calendar publishedDateTime = ConversionUtil.invokeConverter(DateUtil.DATE_UTC_CALENDAR_CONVERTER, syndEntry.getPublishedDate());
        if (publishedDateTime == null) publishedDateTime = ConversionUtil.invokeConverter(DateUtil.DATE_UTC_CALENDAR_CONVERTER, syndFeed.getPublishedDate());
        if (publishedDateTime != null) {
            channelProperties.setProp(StaticChannel.VIEW_CONTENT_LAST_MODIFIED_BY_PATH_PROP + "^$", ConversionUtil.invokeConverter(DateUtil.RFC_1123_CALENDAR_STRING_CONVERTER, publishedDateTime), Locale.ROOT, null);
        }
        final Locale contentLocale = Locale.ROOT;
        final Document contentDocument = DOMUtil.newDocument();
        final Element documentElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, null, "div", contentDocument);
        final List<SyndContent> contents = syndEntry.getContents();
        if (!contents.isEmpty()) {
            int index = -1;
            for (SyndContent content : contents) {
                index++;
                createContentElement(documentElement, syndEntry, content, "feed_content" + ((index > 0) ? '_' + String.valueOf(index + 1) : ""));
            }
        } else {
            final SyndContent description = syndEntry.getDescription();
            createContentElement(documentElement, syndEntry, description, "feed_description");
        }
        final String channelContent = DOMUtil.createLSSerializer(false, true, false).writeToString(contentDocument);
        channelProperties.setProp(StaticChannel.VIEW_CONTENT_VALUE_BY_PATH_PROP + "^$", channelContent, contentLocale, null);
        final ContentManager.LocalChannelDefinition<StaticChannel> channelDefinition = new ContentManager.LocalChannelDefinition<StaticChannel>(localChannelSpecification, channelProperties, feedPageDescriptor.getChannelAccessControl(), StaticChannel.class);
        new ContentManager.ChannelPluginDefinition<ChannelTitle>(channelDefinition, new RSProperties(this, channelDefinition.getProperties()), null, ChannelTitle.class);
        return channelDefinition;
    }
