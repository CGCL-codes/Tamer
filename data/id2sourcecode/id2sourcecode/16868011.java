    protected Element createRestoreControlElement(final Page.Request pageRequest, final Channel.ViewResponse viewResponse, final Element controlElement) throws WWWeeePortal.Exception {
        final ResourceBundle wwweeeResourceBundle = WWWeeePortal.getPortalResourceBundle(pageRequest.getHttpHeaders());
        final Element unmaxElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "a", controlElement);
        getChannel().setIDAndClassAttrs(unmaxElement, Arrays.asList("controls", "restore"), new String[][] { new String[] { getPortal().getPortalID(), "channel", "controls", "maximization" } }, null);
        if (!MiscUtil.equal(wwweeeResourceBundle.getLocale(), viewResponse.getLocale())) DOMUtil.setXMLLangAttr(unmaxElement, wwweeeResourceBundle.getLocale());
        final String unmaxElementTitle = wwweeeResourceBundle.getString("channel_control_restore");
        DOMUtil.createAttribute(null, null, "title", unmaxElementTitle, unmaxElement);
        final String unmaxElementHref = pageRequest.getPage().getKey().getPageURI(pageRequest.getUriInfo(), null, null, false).toString();
        DOMUtil.createAttribute(null, null, "href", unmaxElementHref, unmaxElement);
        DOMUtil.createAttribute(null, null, "rel", "up", unmaxElement);
        final Element unmaxTextElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", unmaxElement);
        getChannel().setIDAndClassAttrs(unmaxTextElement, Arrays.asList("controls", "restore", "text"), null, null);
        DOMUtil.appendText(unmaxTextElement, unmaxElementTitle);
        return unmaxElement;
    }
