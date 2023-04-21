    protected Element createControlElement(final Page.Request pageRequest, final Channel.ViewResponse viewResponse, final Element controlsElement) throws WWWeeePortal.Exception {
        final Element controlElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "li", controlsElement);
        getChannel().setIDAndClassAttrs(controlElement, Arrays.asList("controls", getControlName(pageRequest)), new String[][] { new String[] { getPortal().getPortalID(), "channel", "controls", "control" } }, null);
        return controlElement;
    }
