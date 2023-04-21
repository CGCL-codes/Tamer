    @Override
    public void doViewRequestImpl(final Page.Request pageRequest, final ViewResponse viewResponse) throws WWWeeePortal.Exception {
        final StringBuffer contentSignature = new StringBuffer();
        final Element contentContainerElement = viewResponse.getContentContainerElement();
        final Element pageHeadingElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, getConfigProp(HEADING_ELEMENT_NAME_PROP, pageRequest, "h1", false), contentContainerElement);
        setIDAndClassAttrs(pageHeadingElement, Arrays.asList("heading"), null, null);
        final Element pageHeadingTextElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", pageHeadingElement);
        setIDAndClassAttrs(pageHeadingTextElement, Arrays.asList("heading", "text"), null, null);
        final String[][] extraPageHeadingClasses = new String[][] { new String[] { portal.getPortalID(), "channel", definition.getID() } };
        final String[][] extraPageHeadingDividerClasses = new String[][] { new String[] { portal.getPortalID(), "channel", definition.getID(), "divider" } };
        final String[][] extraPageHeadingTextClasses = new String[][] { new String[] { portal.getPortalID(), "channel", definition.getID(), "text" } };
        final Element portalNameAnchorElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "a", pageHeadingTextElement);
        setIDAndClassAttrs(portalNameAnchorElement, Arrays.asList("heading", "portal", "anchor"), extraPageHeadingClasses, null);
        final Element portalNameAnchorTextElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", portalNameAnchorElement);
        setIDAndClassAttrs(portalNameAnchorTextElement, Arrays.asList("heading", "portal", "anchor", "text"), extraPageHeadingTextClasses, null);
        final String portalName = portal.getName(pageRequest.getSecurityContext(), pageRequest.getHttpHeaders());
        DOMUtil.appendText(portalNameAnchorTextElement, portalName);
        contentSignature.append(portalName);
        final ContentManager.PageDefinition<?> defaultPageDef = CollectionUtil.first(portal.getContentManager().getPageDefinitions(null, null, null, null, null, null, true, pageRequest.getUriInfo(), pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), false, 0, 1), null);
        if ((defaultPageDef != null) && ((!defaultPageDef.getKey().equals(pageRequest.getPage().getKey())) || (pageRequest.getMaximizedChannelKey() != null))) {
            final URI defaultPageURI = defaultPageDef.getKey().getPageURI(pageRequest.getUriInfo(), null, null, false);
            DOMUtil.createAttribute(null, null, "href", defaultPageURI.toString(), portalNameAnchorElement);
            DOMUtil.createAttribute(null, null, "rel", "first", portalNameAnchorElement);
        }
        if (!getConfigProp(PAGE_GROUP_ANCHOR_DISABLE_PROP, pageRequest, RSProperties.RESULT_BOOLEAN_CONVERTER, Boolean.FALSE, false, false).booleanValue()) {
            final Element pageGroupDividerElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", pageHeadingTextElement);
            setIDAndClassAttrs(pageGroupDividerElement, Arrays.asList("heading", "group", "divider"), extraPageHeadingDividerClasses, null);
            DOMUtil.appendText(pageGroupDividerElement, " - ");
            contentSignature.append(" - ");
            final Element pageGroupAnchorElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "a", pageHeadingTextElement);
            setIDAndClassAttrs(pageGroupAnchorElement, Arrays.asList("heading", "group", "anchor"), extraPageHeadingClasses, null);
            final String pageGroupTitle = pageRequest.getPage().getGroupTitleText(pageRequest);
            DOMUtil.appendText(pageGroupAnchorElement, pageGroupTitle);
            contentSignature.append(pageGroupTitle);
            final String pageGroupAnchorHrefProp = getConfigProp(PAGE_GROUP_ANCHOR_HREF_PROP, pageRequest, null, true);
            if (pageGroupAnchorHrefProp != null) {
                DOMUtil.createAttribute(null, null, "href", pageGroupAnchorHrefProp, pageGroupAnchorElement);
                final String pageGroupAnchorTitleProp = getConfigProp(PAGE_GROUP_ANCHOR_TITLE_PROP, pageRequest, null, true);
                if (pageGroupAnchorTitleProp != null) {
                    DOMUtil.createAttribute(null, null, "title", pageGroupAnchorTitleProp, pageGroupAnchorElement);
                }
                final String pageGroupAnchorRelProp = getConfigProp(PAGE_GROUP_ANCHOR_REL_PROP, pageRequest, null, true);
                if (pageGroupAnchorRelProp != null) {
                    DOMUtil.createAttribute(null, null, "rel", pageGroupAnchorRelProp, pageGroupAnchorElement);
                }
            }
        }
        final Element pageTitleDividerElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", pageHeadingTextElement);
        setIDAndClassAttrs(pageTitleDividerElement, Arrays.asList("heading", "page", "divider"), extraPageHeadingDividerClasses, null);
        DOMUtil.appendText(pageTitleDividerElement, " - ");
        contentSignature.append(" - ");
        final Element pageTitleAnchorElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "a", pageHeadingTextElement);
        setIDAndClassAttrs(pageTitleAnchorElement, Arrays.asList("heading", "page", "anchor"), extraPageHeadingClasses, null);
        final Element pageTitleAnchorTextElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", pageTitleAnchorElement);
        setIDAndClassAttrs(pageTitleAnchorTextElement, Arrays.asList("heading", "page", "anchor", "text"), extraPageHeadingTextClasses, null);
        final String pageTitle = pageRequest.getPage().getTitleText(pageRequest);
        DOMUtil.appendText(pageTitleAnchorTextElement, pageTitle);
        contentSignature.append(pageTitle);
        if (pageRequest.getMaximizedChannelKey() != null) {
            final URI pageURI = pageRequest.getPage().getKey().getPageURI(pageRequest.getUriInfo(), null, null, false);
            DOMUtil.createAttribute(null, null, "href", pageURI.toString(), pageTitleAnchorElement);
            DOMUtil.createAttribute(null, null, "rel", "up", pageTitleAnchorElement);
        } else {
            final String pageTitleAnchorHrefProp = getConfigProp(PAGE_TITLE_ANCHOR_HREF_PROP, pageRequest, null, true);
            if (pageTitleAnchorHrefProp != null) {
                DOMUtil.createAttribute(null, null, "href", pageTitleAnchorHrefProp, pageTitleAnchorElement);
                final String pageTitleAnchorTitleProp = getConfigProp(PAGE_TITLE_ANCHOR_TITLE_PROP, pageRequest, null, true);
                if (pageTitleAnchorTitleProp != null) DOMUtil.createAttribute(null, null, "title", pageTitleAnchorTitleProp, pageTitleAnchorElement);
                final String pageTitleAnchorRelProp = getConfigProp(PAGE_TITLE_ANCHOR_REL_PROP, pageRequest, null, true);
                if (pageTitleAnchorRelProp != null) DOMUtil.createAttribute(null, null, "rel", pageTitleAnchorRelProp, pageTitleAnchorElement);
            }
        }
        if (pageRequest.getMaximizedChannelKey() != null) {
            final Element channelTitleDividerElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", pageHeadingTextElement);
            setIDAndClassAttrs(channelTitleDividerElement, Arrays.asList("heading", "channel", "divider"), extraPageHeadingDividerClasses, null);
            DOMUtil.appendText(channelTitleDividerElement, " - ");
            contentSignature.append(" - ");
            final Element channelTitleElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "span", pageHeadingTextElement);
            setIDAndClassAttrs(channelTitleElement, Arrays.asList("heading", "channel", "text"), extraPageHeadingTextClasses, null);
            String channelTitle = null;
            if (!pageRequest.isMaximized(this)) {
                try {
                    final ViewResponse maximizedChannelResponse = Page.getChannelViewResponse(pageRequest.getChannelViewTasks(), pageRequest.getMaximizedChannelKey());
                    if (maximizedChannelResponse != null) {
                        if (!MiscUtil.equal(viewResponse.getLocale(), maximizedChannelResponse.getLocale())) {
                            DOMUtil.setXMLLangAttr(channelTitleElement, maximizedChannelResponse.getLocale());
                        }
                        channelTitle = maximizedChannelResponse.getTitle();
                    }
                } catch (WebApplicationException wae) {
                } catch (WWWeeePortal.Exception wpe) {
                }
            }
            if (channelTitle == null) channelTitle = pageRequest.getPage().getChannel(pageRequest.getMaximizedChannelKey()).getTitleText(pageRequest);
            DOMUtil.appendText(channelTitleElement, channelTitle);
            contentSignature.append(channelTitle);
        }
        final EntityTag entityTag = Page.createEntityTag(pageRequest, viewResponse.getCacheControl(), contentSignature, null, viewResponse.getLastModified(), false);
        if (entityTag != null) viewResponse.setEntityTag(entityTag);
        return;
    }
