    protected Element createHeadTitleElement(final Request request, final Element headElement) throws ConfigManager.ConfigException {
        final Element titleElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "title", headElement);
        final StringBuffer titleText = new StringBuffer();
        titleText.append(portal.getName(request.getSecurityContext(), request.getHttpHeaders()));
        if (!isGroupTitleDisabled(request)) {
            titleText.append(" - ");
            titleText.append(getGroupTitleText(request));
        }
        titleText.append(" - ");
        titleText.append(getTitleText(request));
        if (request.getMaximizedChannelKey() != null) {
            titleText.append(" - ");
            try {
                final Channel.ViewResponse channelViewResponse = getChannelViewResponse(request.getChannelViewTasks(), request.getMaximizedChannelKey());
                titleText.append(channelViewResponse.getTitle());
            } catch (WWWeeePortal.Exception wpe) {
                titleText.append(request.getPage().getChannel(request.getMaximizedChannelKey()).getTitleText(request));
            }
        }
        DOMUtil.appendText(titleElement, titleText.toString());
        return titleElement;
    }
