    @Override
    protected void createAugmentationContent(final Page.Request pageRequest, final Channel.ViewResponse viewResponse, final Element augmentationElement) throws WWWeeePortal.Exception {
        final Map<String, Object> metaProps = new HashMap<String, Object>();
        pageRequest.getMetaProps(metaProps);
        getPortal().getMetaProps(metaProps, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders());
        pageRequest.getPage().getMetaProps(pageRequest, metaProps);
        getChannel().getMetaProps(pageRequest, metaProps);
        getMetaProps(pageRequest, metaProps);
        final boolean isPublishAllEnabled = getConfigProp(PUBLISH_ALL_ENABLE_PROP, pageRequest, RSProperties.RESULT_BOOLEAN_CONVERTER, Boolean.FALSE, false, false).booleanValue();
        final SortedMap<String, String> publishedByNum = (!isPublishAllEnabled) ? ConfigManager.getConfigProps(definition.getProperties(), PUBLISH_BY_NUM_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), RSProperties.RESULT_STRING_CONVERTER, true, true) : null;
        Map<String, Object> publishMetaProps = null;
        if (isPublishAllEnabled) {
            publishMetaProps = metaProps;
        } else {
            publishMetaProps = new HashMap<String, Object>();
            for (String publishMetaProp : publishedByNum.values()) {
                final Object value = metaProps.get(publishMetaProp);
                if (value != null) publishMetaProps.put(publishMetaProp, value);
            }
        }
        final Element propertiesElement = MarkupManager.createMetaPropsPublishElement(augmentationElement, publishMetaProps);
        getChannel().setIDAndClassAttrs(propertiesElement, Arrays.asList("properties"), new String[][] { new String[] { getPortal().getPortalID(), "internal" } }, null);
        DOMUtil.createAttribute(null, null, "title", "Channel Meta-Properties", propertiesElement);
        return;
    }
