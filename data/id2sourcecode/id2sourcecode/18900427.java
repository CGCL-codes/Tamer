    protected Element createSiteNavPageElement(final Page.Request pageRequest, ContentManager.PageDefinition<?> page, final boolean pageIsReqPage, final Element parentElement, final StringBuffer contentSignature, final boolean channelDisplayEnabled) throws WWWeeePortal.Exception {
        final String portalID = portal.getPortalID();
        final String channelID = definition.getID();
        final Element pageElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "li", parentElement);
        final String pageIDAttr = ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "page", page.getContentContainer().getOwnerID(), page.getParentDefinition().getID(), page.getID()));
        DOMUtil.createAttribute(null, null, "id", pageIDAttr, pageElement);
        final StringBuffer pageClassAttr = new StringBuffer();
        pageClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "page")));
        if (pageIsReqPage) {
            pageClassAttr.append(' ');
            pageClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "page", "current")));
        }
        DOMUtil.createAttribute(null, null, "class", pageClassAttr.toString(), pageElement);
        final Element pageTitleElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "div", pageElement);
        final String pageTitleIDAttr = ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "page", "title", page.getContentContainer().getOwnerID(), page.getParentDefinition().getID(), page.getID()));
        DOMUtil.createAttribute(null, null, "id", pageTitleIDAttr, pageTitleElement);
        final StringBuffer pageTitleClassAttr = new StringBuffer();
        pageTitleClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "title")));
        pageTitleClassAttr.append(' ');
        pageTitleClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "page", "title")));
        if (pageIsReqPage) {
            pageTitleClassAttr.append(' ');
            pageTitleClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "title", "current")));
            pageTitleClassAttr.append(' ');
            pageTitleClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "page", "title", "current")));
        }
        DOMUtil.createAttribute(null, null, "class", pageTitleClassAttr.toString(), pageTitleElement);
        createSiteNavPageAnchorElement(pageRequest, page, pageIsReqPage, pageTitleElement, contentSignature);
        createSiteNavPageMetaPropsElement(pageRequest, page, pageIsReqPage, pageElement);
        if (!channelDisplayEnabled) return pageElement;
        final Map<Pattern, Pattern> withMatchingChannelGroupProps = ConversionUtil.invokeConverter(INCLUDE_CHANNEL_GROUP_WITH_PROP_MAP_CONVERTER, ConfigManager.getConfigProps(definition.getProperties(), INCLUDE_CHANNEL_GROUP_WITH_PROP_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), RSProperties.RESULT_STRING_CONVERTER, false, false));
        final Map<Pattern, Pattern> withoutMatchingChannelGroupProps = ConversionUtil.invokeConverter(INCLUDE_CHANNEL_GROUP_WITHOUT_PROP_MAP_CONVERTER, ConfigManager.getConfigProps(definition.getProperties(), INCLUDE_CHANNEL_GROUP_WITHOUT_PROP_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), RSProperties.RESULT_STRING_CONVERTER, false, false));
        final Map<Pattern, Pattern> withMatchingChannelProps = ConversionUtil.invokeConverter(INCLUDE_CHANNEL_WITH_PROP_MAP_CONVERTER, ConfigManager.getConfigProps(definition.getProperties(), INCLUDE_CHANNEL_WITH_PROP_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), RSProperties.RESULT_STRING_CONVERTER, false, false));
        final Map<Pattern, Pattern> withoutMatchingChannelProps = ConversionUtil.invokeConverter(INCLUDE_CHANNEL_WITHOUT_PROP_MAP_CONVERTER, ConfigManager.getConfigProps(definition.getProperties(), INCLUDE_CHANNEL_WITHOUT_PROP_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), RSProperties.RESULT_STRING_CONVERTER, false, false));
        final List<ContentManager.ChannelDefinition<?, ?>> channelDefinitions = portal.getContentManager().getChannelDefinitions(page, withMatchingChannelGroupProps, withoutMatchingChannelGroupProps, withMatchingChannelProps, withoutMatchingChannelProps, withMatchingChannelProps, withoutMatchingChannelProps, true, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders());
        if (channelDefinitions == null) return pageElement;
        final Element channelsElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "ol", pageElement);
        final StringBuffer channelsClassAttr = new StringBuffer();
        channelsClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "page", "channels")));
        channelsClassAttr.append(' ');
        channelsClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "page", "channels", page.getParentDefinition().getID(), page.getID())));
        DOMUtil.createAttribute(null, null, "class", channelsClassAttr.toString(), channelsElement);
        for (ContentManager.ChannelGroupDefinition channelGroupDefinition : CollectionUtil.mkNotNull(page.getMatchingChildDefinitions(null, withMatchingChannelGroupProps, withoutMatchingChannelGroupProps, true, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), -1, false, true))) {
            for (ContentManager.ChannelSpecification<?> channelSpecification : CollectionUtil.mkNotNull(channelGroupDefinition.getMatchingChildDefinitions(null, withMatchingChannelProps, withoutMatchingChannelProps, true, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), -1, false, true))) {
                final boolean channelIsReqChannel;
                final ContentManager.ChannelDefinition<?, ?> channelDefinition;
                if ((pageIsReqPage) && (pageRequest.getMaximizedChannelKey() != null) && (pageRequest.getMaximizedChannelKey().equals(channelSpecification.getKey()))) {
                    channelIsReqChannel = true;
                    channelDefinition = pageRequest.getPage().getChannel(pageRequest.getMaximizedChannelKey()).getDefinition();
                } else {
                    channelIsReqChannel = false;
                    channelDefinition = ContentManager.AbstractContentDefinition.getContentDefinition(channelDefinitions, channelSpecification.getID());
                }
                if (channelDefinition == null) continue;
                createSiteNavChannelElement(pageRequest, page, channelDefinition, channelIsReqChannel, channelsElement, contentSignature);
            }
        }
        return pageElement;
    }
