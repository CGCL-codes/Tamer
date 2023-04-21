    @Override
    protected void createAugmentationContent(final Page.Request pageRequest, final Channel.ViewResponse viewResponse, final Element augmentationElement) throws WWWeeePortal.Exception {
        final Element titleElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, getConfigProp(TITLE_ELEMENT_NAME_PROP, pageRequest, "h2", false), augmentationElement);
        getChannel().setIDAndClassAttrs(titleElement, Arrays.asList("title"), null, null);
        final Element titleAnchorElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "a", titleElement);
        getChannel().setIDAndClassAttrs(titleAnchorElement, Arrays.asList("title", "anchor"), null, null);
        final Element titleTextElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", titleAnchorElement);
        getChannel().setIDAndClassAttrs(titleTextElement, Arrays.asList("title", "text"), null, null);
        DOMUtil.appendText(titleTextElement, viewResponse.getTitle());
        final String titleAnchorHrefProp = getConfigProp(TITLE_ANCHOR_HREF_PROP, pageRequest, null, true);
        if (titleAnchorHrefProp != null) DOMUtil.createAttribute(null, null, "href", titleAnchorHrefProp, titleAnchorElement);
        final String titleAnchorTitleProp = getConfigProp(TITLE_ANCHOR_TITLE_PROP, pageRequest, null, true);
        if (titleAnchorTitleProp != null) DOMUtil.createAttribute(null, null, "title", titleAnchorTitleProp, titleAnchorElement);
        final String titleAnchorRelProp = getConfigProp(TITLE_ANCHOR_REL_PROP, pageRequest, null, true);
        if (titleAnchorRelProp != null) DOMUtil.createAttribute(null, null, "rel", titleAnchorRelProp, titleAnchorElement);
        return;
    }
