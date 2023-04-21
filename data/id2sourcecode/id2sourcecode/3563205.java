    protected Header getHeaderOverrideByPath(final String headerName, final Page.Request pageRequest) throws WWWeeePortal.Exception {
        final Map.Entry<RSProperties.Entry<String>, Map.Entry<String, Pattern>> headerOverride = CollectionUtil.first(CollectionUtil.keySet(ConfigManager.RegexPropKeyConverter.getMatchingValues(ConfigManager.getConfigProps(definition.getProperties(), HEADER_OVERRIDE_BY_PATH_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), HEADER_OVERRIDE_BY_PATH_CONVERTER, true, true), headerName, false, StringUtil.toString(pageRequest.getChannelLocalPath(getChannel()), null))), null);
        return (headerOverride != null) ? new BasicHeader(headerOverride.getValue().getKey(), headerOverride.getKey().getValue()) : null;
    }
