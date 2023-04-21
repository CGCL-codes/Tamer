    protected String createCachedResponseClientAttributesKey(final Page.Request pageRequest, final String mode) throws WWWeeePortal.Exception {
        final List<String> qualifiers = new ArrayList<String>(5);
        qualifiers.add(getEffectiveRequestMethod(pageRequest));
        qualifiers.add(StringUtil.toString((isCachePerLanguageEnabled(pageRequest)) ? CollectionUtil.toString(pageRequest.getHttpHeaders().getAcceptableLanguages(), null, null, ",") : null, ""));
        qualifiers.add(mode);
        qualifiers.add(StringUtil.toString(pageRequest.getChannelLocalPath(getChannel()), ""));
        qualifiers.add(StringUtil.toString(((isCachePerQueryEnabled(pageRequest)) && (pageRequest.isMaximized(getChannel()))) ? ConversionUtil.invokeConverter(NetUtil.QUERY_PARAMS_URL_ENCODE_CONVERTER, new TreeMap<String, List<String>>(pageRequest.getUriInfo().getQueryParameters())) : null, ""));
        return createClientAttributesKey(pageRequest, "CachedResponse", isCachePerPageEnabled(pageRequest), qualifiers);
    }
