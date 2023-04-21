    @Override
    public void doViewRequestImpl(final Page.Request pageRequest, final ViewResponse viewResponse) throws WWWeeePortal.Exception {
        final String path = StringUtil.toString(pageRequest.getChannelLocalPath(this), null);
        final MediaType type = getViewContentType(pageRequest, path);
        if (type != null) viewResponse.setContentType(type);
        final Date lastModified = getViewContentLastModified(pageRequest, path);
        if (lastModified != null) viewResponse.setLastModified(lastModified);
        final Integer maxAge = getViewContentMaxAge(pageRequest, path);
        if (maxAge != null) {
            final CacheControl cacheControl = ConversionUtil.invokeConverter(RESTUtil.CACHE_CONTROL_DEFAULT_CONVERTER, viewResponse.getCacheControl());
            cacheControl.setMaxAge(maxAge.intValue());
            viewResponse.setCacheControl(cacheControl);
        }
        final Map.Entry<RSProperties.Entry<Document>, Map.Entry<String, Pattern>> content = getViewContentValue(pageRequest, path);
        EntityTag entityTag = getViewContentEntityTag(pageRequest, path);
        if (entityTag == null) {
            String contentSignature = null;
            if (content != null) {
                final Document document = content.getKey().getValue();
                final MD5DigestOutputStream md5dos = new MD5DigestOutputStream();
                synchronized (document) {
                    DOMUtil.createLSSerializer(true, true, false).write(document, ConversionUtil.invokeConverter(DOMUtil.OUTPUT_STREAM_LS_OUTPUT_CONVERTER, md5dos));
                }
                contentSignature = md5dos.getDigestHexString();
            }
            entityTag = Page.createEntityTag(pageRequest, viewResponse.getCacheControl(), contentSignature, null, lastModified, false);
        }
        if (entityTag != null) viewResponse.setEntityTag(entityTag);
        if (content != null) {
            viewResponse.setLocale(content.getKey().getValueLocale());
            final Element contentContainerElement = viewResponse.getContentContainerElement();
            final Document document = content.getKey().getValue();
            if (document.getDocumentElement() != null) DOMUtil.appendChild(contentContainerElement, document.getDocumentElement());
        }
        return;
    }
