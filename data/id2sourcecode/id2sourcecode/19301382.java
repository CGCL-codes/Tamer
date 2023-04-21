    protected Response buildResponse(final Request request, final TransformableDocument responseDocument) throws WWWeeePortal.Exception {
        Date lastModified = null;
        Date expires = null;
        CacheControl cacheControl = ConversionUtil.invokeConverter(RESTUtil.CACHE_CONTROL_DEFAULT_CONVERTER, getCacheControlDefault(request));
        int maxAge = cacheControl.getMaxAge();
        int sMaxAge = cacheControl.getSMaxAge();
        boolean weakETag = false;
        final MD5DigestOutputStream md5dos = new MD5DigestOutputStream();
        final PrintStream md5ps = new PrintStream(md5dos);
        for (ContentManager.ChannelGroupDefinition channelGroup : CollectionUtil.mkNotNull(definition.getMatchingChildDefinitions(null, null, null, true, request.getSecurityContext(), request.getHttpHeaders(), -1, false, true))) {
            if (channelGroup == null) continue;
            md5ps.append(channelGroup.getID());
            md5ps.append('{');
            for (ContentManager.ChannelSpecification<?> channelSpecification : CollectionUtil.mkNotNull(channelGroup.getMatchingChildDefinitions(null, null, null, true, request.getSecurityContext(), request.getHttpHeaders(), -1, false, true))) {
                if (channelSpecification == null) continue;
                final Channel.ViewResponse channelViewResponse = getChannelViewResponse(request.getChannelViewTasks(), channelSpecification.getKey());
                if (channelViewResponse == null) continue;
                md5ps.append(channelSpecification.getID());
                md5ps.append('{');
                final Date channelLastModified = channelViewResponse.getLastModified();
                if (channelLastModified != null) {
                    if (lastModified == null) {
                        lastModified = channelLastModified;
                    } else if (channelLastModified.after(lastModified)) {
                        lastModified = channelLastModified;
                    }
                }
                final Date channelExpires = channelViewResponse.getExpires();
                if (channelExpires != null) {
                    if (expires == null) {
                        expires = channelExpires;
                    } else if (channelExpires.before(expires)) {
                        expires = channelExpires;
                    }
                }
                final CacheControl channelCacheControl = channelViewResponse.getCacheControl();
                if (channelCacheControl != null) {
                    if (channelCacheControl.isPrivate()) {
                        cacheControl.setPrivate(true);
                        cacheControl.getCacheExtension().remove("public");
                    }
                    if (channelCacheControl.isNoCache()) cacheControl.setNoCache(true);
                    if (channelCacheControl.isNoStore()) cacheControl.setNoStore(true);
                    if (channelCacheControl.isNoTransform()) cacheControl.setNoTransform(true);
                    if (channelCacheControl.isMustRevalidate()) cacheControl.setMustRevalidate(true);
                    if (channelCacheControl.isProxyRevalidate()) cacheControl.setProxyRevalidate(true);
                    final int channelMaxAge = channelCacheControl.getMaxAge();
                    if (channelMaxAge >= 0) {
                        if (maxAge < 0) {
                            maxAge = channelMaxAge;
                        } else if (channelMaxAge < maxAge) {
                            maxAge = channelMaxAge;
                        }
                    }
                    final int channelSMaxAge = channelCacheControl.getSMaxAge();
                    if (channelSMaxAge >= 0) {
                        if (sMaxAge < 0) {
                            sMaxAge = channelSMaxAge;
                        } else if (channelSMaxAge < sMaxAge) {
                            sMaxAge = channelSMaxAge;
                        }
                    }
                }
                final EntityTag channelEntityTag = channelViewResponse.getEntityTag();
                if (channelEntityTag != null) {
                    if (channelEntityTag.isWeak()) weakETag = true;
                    md5ps.append(channelEntityTag.getValue());
                } else {
                    weakETag = true;
                }
                md5ps.append('}');
            }
            md5ps.append('}');
        }
        cacheControl.setMaxAge(maxAge);
        cacheControl.setSMaxAge(sMaxAge);
        cacheControl = ConversionUtil.invokeConverter(RESTUtil.CACHE_CONTROL_NULL_CONVERTER, cacheControl);
        final EntityTag entityTag = createEntityTag(request, cacheControl, md5dos.getDigestHexString(), null, lastModified, weakETag);
        Response.ResponseBuilder responseBuilder = RESTUtil.evaluatePreconditions(request.getRSRequest(), lastModified, entityTag);
        if (responseBuilder != null) return responseBuilder.build();
        responseBuilder = Response.ok();
        responseBuilder.lastModified(lastModified);
        responseBuilder.expires(expires);
        responseBuilder.tag(entityTag);
        responseBuilder.cacheControl(cacheControl);
        responseBuilder.entity(responseDocument);
        return responseBuilder.build();
    }
