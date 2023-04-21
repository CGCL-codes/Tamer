    protected Element createControlsElement(final Page.Request pageRequest, final Channel.ViewResponse viewResponse, final Element augmentationElement) throws WWWeeePortal.Exception {
        final String channelControlsID = ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(getPortal().getPortalID(), "channel", "controls", getChannel().getDefinition().getID()));
        final Element existingChannelControlsElement = DOMUtil.getChildElement(augmentationElement, HTMLUtil.HTML_NS_URI, "ol", null, "id", channelControlsID);
        if (existingChannelControlsElement != null) return existingChannelControlsElement;
        final Element controlsElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "ol", augmentationElement);
        getChannel().setIDAndClassAttrs(controlsElement, Arrays.asList("controls"), null, null);
        return controlsElement;
    }
