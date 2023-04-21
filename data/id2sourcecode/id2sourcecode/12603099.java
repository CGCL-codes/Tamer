    protected Channel.ViewResponse getCachedResponseIfValid(final Page.Request pageRequest, final String mode) throws WWWeeePortal.Exception {
        if (!getChannel().isCacheControlClientDirectivesDisabled(pageRequest)) {
            final CacheControl cacheControl = ConversionUtil.invokeConverter(RESTUtil.HTTP_HEADERS_CACHE_CONTROL_CONVERTER, pageRequest.getHttpHeaders());
            if (cacheControl != null) {
                if (cacheControl.isNoStore()) return null;
                if (cacheControl.isNoCache()) return null;
            }
        }
        final Channel.ViewResponse cachedResponse = getCachedResponse(pageRequest, mode);
        if (cachedResponse == null) return null;
        if ((cachedResponse.getCacheControl() != null) && (cachedResponse.getCacheControl().getMaxAge() >= 0) && (System.currentTimeMillis() > cachedResponse.getDate().getTime() + (1000L * cachedResponse.getCacheControl().getMaxAge()))) return null;
        if ((cachedResponse.getExpires() != null) && (System.currentTimeMillis() > cachedResponse.getExpires().getTime())) return null;
        return cachedResponse;
    }
