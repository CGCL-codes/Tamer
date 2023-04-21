    protected final void setBodyIDAndClassAttrs(final Element bodyElement, final Request request) throws WWWeeePortal.Exception {
        final String portalID = portal.getPortalID();
        final ArrayList<String> idComponents = new ArrayList<String>();
        idComponents.add(portalID);
        idComponents.add("page");
        idComponents.add(definition.getKey().getOwnerID());
        idComponents.add(definition.getKey().getPageGroupID());
        idComponents.add(definition.getKey().getPageID());
        if (request.getMaximizedChannelKey() != null) idComponents.add(request.getMaximizedChannelKey().getChannelID());
        final String idValue = ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, idComponents);
        DOMUtil.createAttribute(null, null, "id", idValue, bodyElement);
        final StringBuffer classBuffer = new StringBuffer();
        classBuffer.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "page")));
        classBuffer.append(' ');
        classBuffer.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "page", "owner", definition.getKey().getOwnerID())));
        classBuffer.append(' ');
        classBuffer.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "page", "group", definition.getKey().getPageGroupID())));
        classBuffer.append(' ');
        classBuffer.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "page", definition.getKey().getOwnerID(), definition.getKey().getPageGroupID(), definition.getKey().getPageID())));
        if (request.getMaximizedChannelKey() != null) {
            classBuffer.append(' ');
            classBuffer.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portalID, "page", "maximized")));
        }
        for (String classAttr : ConfigManager.getConfigProps(definition.getProperties(), CLASS_BY_NUM_PATTERN, request.getSecurityContext(), request.getHttpHeaders(), RSProperties.RESULT_STRING_CONVERTER, false, false).values()) {
            classBuffer.append(' ');
            classBuffer.append(classAttr);
        }
        DOMUtil.createAttribute(null, null, "class", classBuffer.toString(), bodyElement);
        return;
    }
