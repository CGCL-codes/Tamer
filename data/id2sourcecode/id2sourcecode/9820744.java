    protected Element createAugmentationElement(final Page.Request pageRequest, final Channel.ViewResponse viewResponse) throws WWWeeePortal.Exception {
        final String augmentationName = getAugmentationName(pageRequest);
        final String augmentationID = ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(getPortal().getPortalID(), "channel", augmentationName, getChannel().getDefinition().getID()));
        final String sectionType = getSectionType(pageRequest);
        final boolean isHTML5Supported = getPortal().isHTML5Supported(pageRequest.getSecurityContext(), pageRequest.getHttpHeaders());
        final String augmentationType = isHTML5Supported ? sectionType : "div";
        final boolean isAppendEnabled = isAppendEnabled(pageRequest);
        final Element responseRootElement = viewResponse.getResponseRootElement();
        final Element existingAugmentationElement = DOMUtil.getChildElement(responseRootElement, HTMLUtil.HTML_NS_URI, augmentationType, null, "id", augmentationID);
        if (existingAugmentationElement != null) return existingAugmentationElement;
        final Element augmentationElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, augmentationType, responseRootElement, DOMUtil.getDocument(responseRootElement), false, false);
        getChannel().setIDAndClassAttrs(augmentationElement, Arrays.asList(augmentationName), isHTML5Supported ? null : new String[][] { new String[] { sectionType } }, null);
        synchronized (DOMUtil.getDocument(responseRootElement)) {
            if (isAppendEnabled) {
                responseRootElement.appendChild(augmentationElement);
            } else {
                responseRootElement.insertBefore(augmentationElement, responseRootElement.getFirstChild());
            }
        }
        return augmentationElement;
    }
