    @Override
    protected Response doResourceRequestImpl(final Page.Request pageRequest) throws WWWeeePortal.Exception, WebApplicationException {
        final String path = StringUtil.toString(pageRequest.getChannelLocalPath(this), null);
        final Map.Entry<RSProperties.Entry<DataSource>, Map.Entry<String, Pattern>> content = getResourceContentValue(pageRequest, path);
        if (content == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        final Date lastModified = getResourceContentLastModified(pageRequest, path);
        CacheControl cacheControl = getCacheControlDefault();
        final Integer maxAge = getResourceContentMaxAge(pageRequest, path);
        if (maxAge != null) {
            cacheControl = ConversionUtil.invokeConverter(RESTUtil.CACHE_CONTROL_DEFAULT_CONVERTER, cacheControl);
            cacheControl.setMaxAge(maxAge.intValue());
        }
        EntityTag entityTag = getResourceContentEntityTag(pageRequest, path);
        if (entityTag == null) {
            final MD5DigestOutputStream md5dos = new MD5DigestOutputStream();
            final DataSource dataSource = content.getKey().getValue();
            try {
                final InputStream inputStream = dataSource.getInputStream();
                IOUtil.pipe(inputStream, md5dos, -1, false, -1);
            } catch (IOException ioe) {
            }
            entityTag = Page.createEntityTag(pageRequest, cacheControl, md5dos.getDigestHexString(), null, lastModified, false);
        }
        Response.ResponseBuilder responseBuilder = RESTUtil.evaluatePreconditions(pageRequest.getRSRequest(), lastModified, entityTag);
        if (responseBuilder != null) return responseBuilder.build();
        responseBuilder = Response.ok(content.getKey().getValue(), getResourceContentType(pageRequest, path));
        responseBuilder.lastModified(getResourceContentLastModified(pageRequest, path));
        responseBuilder.tag(entityTag);
        responseBuilder.cacheControl(cacheControl);
        responseBuilder.language(content.getKey().getValueLocale());
        return responseBuilder.build();
    }
