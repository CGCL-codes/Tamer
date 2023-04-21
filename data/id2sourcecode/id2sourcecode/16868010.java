    protected Element createMaximizeControlElement(final Page.Request pageRequest, final Channel.ViewResponse viewResponse, final Element controlElement) throws WWWeeePortal.Exception {
        final ResourceBundle wwweeeResourceBundle = WWWeeePortal.getPortalResourceBundle(pageRequest.getHttpHeaders());
        final Element maxElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "a", controlElement);
        getChannel().setIDAndClassAttrs(maxElement, Arrays.asList("controls", "maximize"), new String[][] { new String[] { getPortal().getPortalID(), "channel", "controls", "maximization" } }, null);
        if (!MiscUtil.equal(wwweeeResourceBundle.getLocale(), viewResponse.getLocale())) DOMUtil.setXMLLangAttr(maxElement, wwweeeResourceBundle.getLocale());
        final String maxElementTitle = wwweeeResourceBundle.getString("channel_control_maximize");
        DOMUtil.createAttribute(null, null, "title", maxElementTitle, maxElement);
        final ContentManager.ChannelSpecification<?> channelSpecification = pageRequest.getChannelSpecification(getChannel());
        final String maxElementHref = channelSpecification.getKey().getChannelURI(pageRequest.getUriInfo(), Channel.VIEW_MODE, null, null, null, false).toString();
        DOMUtil.createAttribute(null, null, "href", maxElementHref, maxElement);
        final Element maxTextElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", maxElement);
        getChannel().setIDAndClassAttrs(maxTextElement, Arrays.asList("controls", "maximize", "text"), null, null);
        DOMUtil.appendText(maxTextElement, maxElementTitle);
        return maxElement;
    }
