    protected Set<Map.Entry<RSProperties.Entry<String>, Map.Entry<String, Pattern>>> getHeaderAdditionPropsByPath(final Page.Request pageRequest) throws WWWeeePortal.Exception {
        return CollectionUtil.keySet(ConfigManager.RegexPropKeyConverter.getMatchingValues(ConfigManager.getConfigProps(definition.getProperties(), HEADER_ADD_BY_PATH_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), HEADER_ADD_BY_PATH_CONVERTER, true, true), null, false, StringUtil.toString(pageRequest.getChannelLocalPath(getChannel()), null)));
    }
