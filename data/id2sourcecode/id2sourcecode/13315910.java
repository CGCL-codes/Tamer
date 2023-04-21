    protected List<Map.Entry<URL, Templates>> getTransformations(final Page.Request pageRequest) throws WWWeeePortal.Exception {
        final ArrayList<Map.Entry<URL, Templates>> transformations = new ArrayList<Map.Entry<URL, Templates>>();
        CollectionUtil.addAll(transformations, ConfigManager.getConfigProps(definition.getProperties(), VIEW_TRANSFORM_BY_NUM_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), getPortal().getMarkupManager().getTransformByNumPropConverter(), true, true).values());
        CollectionUtil.addAll(transformations, CollectionUtil.values(ConfigManager.RegexPropKeyConverter.getMatchingValues(ConfigManager.getConfigProps(definition.getProperties(), VIEW_TRANSFORM_BY_PATH_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), getPortal().getMarkupManager().getTransformByPathPropConverter(), true, true), null, false, StringUtil.toString(pageRequest.getChannelLocalPath(getChannel()), null))));
        return (!transformations.isEmpty()) ? transformations : null;
    }
