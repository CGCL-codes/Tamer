    @SuppressWarnings("unchecked")
    protected ContentManager.PageDefinition<?> createPageDefinitionFromFeed(final FeedPageDescriptor feedPageDescriptor, final SyndFeed syndFeed) throws WWWeeePortal.Exception {
        if (feedPageDescriptor == null) throw new IllegalArgumentException("null feedPageDescriptor");
        if (syndFeed == null) throw new IllegalArgumentException("null syndFeed");
        final ContentManager.PageContentContainer contentContainer = feedPageDescriptor.getPageGroupDefinition().getParentDefinition().duplicate(null, null, null, false, null);
        final ContentManager.PageGroupDefinition pageGroupDefinition = feedPageDescriptor.getPageGroupDefinition().duplicate(contentContainer, null, false, null, false, null, false, null);
        final ContentManager.PageDefinition<?> templatePageDefinition = getTemplatePageDefinition(feedPageDescriptor.getPageKey().getPageGroupID(), feedPageDescriptor.getPageKey().getPageID());
        final RSProperties pageProperties = new RSProperties(this, pageGroupDefinition.getProperties());
        if (templatePageDefinition != null) pageProperties.putAll(templatePageDefinition.getProperties());
        pageProperties.putAll(feedPageDescriptor.getPageProperties());
        if (syndFeed.getUri() != null) pageProperties.setProp("Feed.URI", syndFeed.getUri(), Locale.ROOT, null);
        if (syndFeed.getTitle() != null) pageProperties.setProp("Feed.Title", syndFeed.getTitle(), Locale.ROOT, null);
        if (syndFeed.getDescription() != null) pageProperties.setProp("Feed.Description", syndFeed.getDescription(), Locale.ROOT, null);
        if (syndFeed.getAuthor() != null) pageProperties.setProp("Feed.Author", syndFeed.getAuthor(), Locale.ROOT, null);
        if (syndFeed.getCopyright() != null) pageProperties.setProp("Feed.Copyright", syndFeed.getCopyright(), Locale.ROOT, null);
        if (syndFeed.getLanguage() != null) pageProperties.setProp("Feed.Language", syndFeed.getLanguage(), Locale.ROOT, null);
        if (syndFeed.getPublishedDate() != null) pageProperties.setProp("Feed.PublishedDate", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, ConversionUtil.invokeConverter(DateUtil.DATE_UTC_CALENDAR_CONVERTER, syndFeed.getPublishedDate())), Locale.ROOT, null);
        final String alink = syndFeed.getLink();
        if (alink != null) {
            pageProperties.setProp("Feed.Link", alink, Locale.ROOT, null);
            if (!disableFeedTitleLinks) {
                pageProperties.setProp(PageHeadingChannel.PAGE_TITLE_ANCHOR_HREF_PROP, alink, Locale.ROOT, null);
                if (syndFeed.getTitle() != null) pageProperties.setProp(PageHeadingChannel.PAGE_TITLE_ANCHOR_TITLE_PROP, syndFeed.getTitle(), Locale.ROOT, null);
            }
        }
        final List<SyndLink> links = syndFeed.getLinks();
        if (links != null) {
            for (SyndLink link : links) {
                final String name = "Feed.Link" + ((link.getRel() != null) ? '.' + link.getRel() : "");
                pageProperties.setProp(name, link.getHref(), Locale.ROOT, null);
                if ((!disableFeedTitleLinks) && ("alternate".equalsIgnoreCase(link.getRel()))) {
                    pageProperties.setProp(PageHeadingChannel.PAGE_TITLE_ANCHOR_HREF_PROP, link.getHref(), Locale.ROOT, null);
                    pageProperties.setProp(PageHeadingChannel.PAGE_TITLE_ANCHOR_REL_PROP, link.getRel(), Locale.ROOT, null);
                    if (link.getTitle() != null) {
                        pageProperties.setProp(PageHeadingChannel.PAGE_TITLE_ANCHOR_TITLE_PROP, link.getTitle(), Locale.ROOT, null);
                    } else if (syndFeed.getTitle() != null) {
                        pageProperties.setProp(PageHeadingChannel.PAGE_TITLE_ANCHOR_TITLE_PROP, syndFeed.getTitle(), Locale.ROOT, null);
                    }
                }
            }
        }
        final ContentManager.PageDefinition<Page> pageDefinition = new ContentManager.PageDefinition<Page>(pageGroupDefinition, pageProperties, feedPageDescriptor.getPageKey().getPageID(), feedPageDescriptor.getPageAccessControl(), Page.class);
        final List<SyndEntry> entries = syndFeed.getEntries();
        ContentManager.ChannelSpecification<?> placeholderChannel = null;
        if (templatePageDefinition != null) {
            final List<ContentManager.ChannelSpecification<?>> templateChannels = templatePageDefinition.getChannelSpecifications();
            if (templateChannels != null) {
                ContentManager.ChannelGroupDefinition currentTargetGroup = null;
                for (ContentManager.ChannelSpecification<?> templateChannel : templateChannels) {
                    if ("feed_entries".equals(templateChannel.getID())) {
                        placeholderChannel = templateChannel;
                        break;
                    }
                    final ContentManager.ChannelGroupDefinition templateGroup = templateChannel.getParentDefinition();
                    if ((currentTargetGroup == null) || (!currentTargetGroup.getID().equals(templateGroup.getID()))) {
                        currentTargetGroup = templateGroup.duplicate(pageDefinition, null, false, null);
                    }
                    templateChannel.duplicate(currentTargetGroup, null);
                }
            }
        }
        final ContentManager.ChannelGroupDefinition targetChannelGroup;
        if (placeholderChannel != null) {
            final ContentManager.ChannelGroupDefinition existingTargetGroup = pageDefinition.getChildDefinition(placeholderChannel.getParentDefinition().getID());
            targetChannelGroup = (existingTargetGroup != null) ? existingTargetGroup : placeholderChannel.getParentDefinition().duplicate(pageDefinition, null, false, null);
        } else {
            final ContentManager.ChannelGroupDefinition existingTargetGroup = pageDefinition.getChildDefinition("body");
            targetChannelGroup = (existingTargetGroup != null) ? existingTargetGroup : new ContentManager.ChannelGroupDefinition(pageDefinition, new RSProperties(this, pageDefinition.getProperties()), "body", null);
        }
        int entryIndex = -1;
        for (SyndEntry entry : entries) {
            entryIndex++;
            if (feedPageDescriptor.createProxyChannels()) {
                createProxyChannelDefinitionFromEntry(feedPageDescriptor, syndFeed, targetChannelGroup, entry, entryIndex);
            } else {
                createStaticChannelDefinitionFromEntry(feedPageDescriptor, syndFeed, targetChannelGroup, entry, entryIndex);
            }
        }
        if (templatePageDefinition != null) {
            final List<ContentManager.ChannelSpecification<?>> templateChannels = templatePageDefinition.getChannelSpecifications();
            if (templateChannels != null) {
                ContentManager.ChannelGroupDefinition currentTargetGroup = null;
                boolean afterPlaceholder = false;
                for (ContentManager.ChannelSpecification<?> templateChannel : templateChannels) {
                    if (!afterPlaceholder) {
                        if ("feed_entries".equals(templateChannel.getID())) afterPlaceholder = true;
                        continue;
                    }
                    final ContentManager.ChannelGroupDefinition templateGroup = templateChannel.getParentDefinition();
                    if ((currentTargetGroup == null) || (!currentTargetGroup.getID().equals(templateGroup.getID()))) {
                        currentTargetGroup = templateGroup.duplicate(pageDefinition, null, false, null);
                    }
                    templateChannel.duplicate(currentTargetGroup, null);
                }
            }
        }
        contentContainer.init();
        return pageDefinition;
    }
