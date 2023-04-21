    protected Element createSiteNavChannelAnchorElement(final Page.Request pageRequest, final ContentManager.PageDefinition<?> page, final ContentManager.ChannelDefinition<?, ?> channel, final boolean channelIsReqChannel, final Element parentElement, final StringBuffer contentSignature) throws WWWeeePortal.Exception {
        final String portalID = portal.getPortalID();
        final String channelID = definition.getID();
        final String channelTitle = ConfigManager.getConfigProp(channel.getProperties(), TITLE_TEXT_PROP, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), null, RSProperties.RESULT_STRING_CONVERTER, channel.getID(), false, false);
        final Element channelAnchorElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "a", parentElement);
        final String channelAnchorIDAttr = ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "channel", "anchor", page.getContentContainer().getOwnerID(), page.getParentDefinition().getID(), page.getID(), channel.getID()));
        DOMUtil.createAttribute(null, null, "id", channelAnchorIDAttr, channelAnchorElement);
        final StringBuffer channelAnchorClassAttr = new StringBuffer();
        channelAnchorClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "anchor")));
        channelAnchorClassAttr.append(' ');
        channelAnchorClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "channel", "anchor")));
        if (channelIsReqChannel) {
            channelAnchorClassAttr.append(' ');
            channelAnchorClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "anchor", "current")));
            channelAnchorClassAttr.append(' ');
            channelAnchorClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "channel", "anchor", "current")));
        }
        DOMUtil.createAttribute(null, null, "class", channelAnchorClassAttr.toString(), channelAnchorElement);
        final boolean channelMaximizationDisabled = ConfigManager.getConfigProp(channel.getProperties(), MAXIMIZATION_DISABLE_PROP, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), null, RSProperties.RESULT_BOOLEAN_CONVERTER, Boolean.FALSE, false, false).booleanValue();
        if ((!channelIsReqChannel) && (!channelMaximizationDisabled)) {
            final ContentManager.ChannelSpecification<?> channelSpecification = page.getChannelSpecification(channel.getID());
            final URI channelHrefURI = channelSpecification.getKey().getChannelURI(pageRequest.getUriInfo(), VIEW_MODE, null, null, null, false);
            DOMUtil.createAttribute(null, null, "href", channelHrefURI.toString(), channelAnchorElement);
        }
        final Element channelAnchorTitleElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", channelAnchorElement);
        final String channelAnchorTitleIDAttr = ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "channel", "anchor", "text", page.getContentContainer().getOwnerID(), page.getParentDefinition().getID(), page.getID(), channel.getID()));
        DOMUtil.createAttribute(null, null, "id", channelAnchorTitleIDAttr, channelAnchorTitleElement);
        final StringBuffer channelAnchorTitleClassAttr = new StringBuffer();
        channelAnchorTitleClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "anchor", "text")));
        channelAnchorTitleClassAttr.append(' ');
        channelAnchorTitleClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "channel", "anchor", "text")));
        if (channelIsReqChannel) {
            channelAnchorTitleClassAttr.append(' ');
            channelAnchorTitleClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "anchor", "text", "current")));
            channelAnchorTitleClassAttr.append(' ');
            channelAnchorTitleClassAttr.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "channel", channelID, "channel", "anchor", "text", "current")));
        }
        DOMUtil.createAttribute(null, null, "class", channelAnchorTitleClassAttr.toString(), channelAnchorTitleElement);
        DOMUtil.appendText(channelAnchorTitleElement, channelTitle);
        contentSignature.append('[');
        contentSignature.append(page.getParentDefinition().getID());
        contentSignature.append('.');
        contentSignature.append(page.getID());
        contentSignature.append('.');
        contentSignature.append(channel.getID());
        contentSignature.append("='");
        contentSignature.append(channelTitle);
        contentSignature.append("']");
        return channelAnchorElement;
    }
