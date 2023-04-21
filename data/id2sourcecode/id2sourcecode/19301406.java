        public Map<String, Object> getMetaProps(final Map<String, Object> metaProps) throws ConfigManager.ConfigException {
            if (metaProps == null) throw new IllegalArgumentException("null metaProps");
            final Map<String, List<String>> requestParams = uriInfo.getQueryParameters();
            if (requestParams != null) {
                for (String requestParamKey : requestParams.keySet()) {
                    final List<String> requestParamValues = requestParams.get(requestParamKey);
                    if (requestParamValues == null) continue;
                    for (int i = 0; i < requestParamValues.size(); i++) {
                        metaProps.put("WWWeee.Request.Parameter." + requestParamKey + ((i > 0) ? '.' + String.valueOf(i + 1) : ""), requestParamValues.get(i));
                    }
                }
            }
            if (attributes != null) {
                for (String key : attributes.keySet()) {
                    final Object value = attributes.get(key);
                    metaProps.put("WWWeee.Request.Attribute." + key, value);
                }
            }
            if (sessionAttributes != null) {
                for (String key : sessionAttributes.keySet()) {
                    final Object value = sessionAttributes.get(key);
                    metaProps.put("WWWeee.Session.Attribute." + key, value);
                }
            }
            int localeIndex = 0;
            for (Locale locale : CollectionUtil.mkNotNull(httpHeaders.getAcceptableLanguages())) {
                metaProps.put("WWWeee.Request.Locale." + String.valueOf(++localeIndex), ConversionUtil.invokeConverter(I18nUtil.LOCALE_LANGUAGE_TAG_CONVERTER, locale));
                if (!locale.getLanguage().isEmpty()) metaProps.put("WWWeee.Request.Locale." + String.valueOf(localeIndex) + ".Language", locale.getLanguage());
                if (!locale.getCountry().isEmpty()) metaProps.put("WWWeee.Request.Locale." + String.valueOf(localeIndex) + ".Country", locale.getCountry());
                if (!locale.getVariant().isEmpty()) metaProps.put("WWWeee.Request.Locale." + String.valueOf(localeIndex) + ".Variant", locale.getVariant());
            }
            final Principal remoteUser = securityContext.getUserPrincipal();
            if (remoteUser != null) metaProps.put("WWWeee.Request.RemoteUser", remoteUser.getName());
            metaProps.put("WWWeee.Request", rsRequest);
            metaProps.put("WWWeee.UriInfo", uriInfo);
            metaProps.put("WWWeee.SecurityContext", securityContext);
            metaProps.put("WWWeee.HttpHeaders", httpHeaders);
            if (maximizedChannelKey != null) {
                metaProps.put("WWWeee.Request.MaximizedChannel.Key", maximizedChannelKey);
                metaProps.put("WWWeee.Request.MaximizedChannel.ID", maximizedChannelKey.getChannelID());
            }
            return metaProps;
        }
