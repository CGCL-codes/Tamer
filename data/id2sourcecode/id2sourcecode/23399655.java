    public void testFetchContentWithMarker() throws Exception {
        oauth.httpResponse = CACHEABLE;
        HttpResponse httpResponse = requestPipeline.execute(signedRequest);
        assertEquals(httpResponse, CACHEABLE);
        assertEquals(cacheProvider.createCache(DefaultHttpCache.CACHE_NAME).getSize(), 1);
        service.invalidateUserResources(ImmutableSet.of("OwnerX"), appxToken);
        oauth.httpResponse = new HttpResponseBuilder(CACHEABLE).setResponseString("NEWCONTENT1").create();
        httpResponse = requestPipeline.execute(signedRequest);
        assertEquals(httpResponse.getResponseAsString(), "NEWCONTENT1");
        assertEquals(httpResponse.getHeader(DefaultInvalidationService.INVALIDATION_HEADER), "o=1;");
        assertEquals(cacheProvider.createCache(DefaultHttpCache.CACHE_NAME).getSize(), 1);
        service.invalidateUserResources(ImmutableSet.of("ViewerX"), appxToken);
        oauth.httpResponse = new HttpResponseBuilder(CACHEABLE).setResponseString("NEWCONTENT2").create();
        httpResponse = requestPipeline.execute(signedRequest);
        assertEquals(httpResponse.getResponseAsString(), "NEWCONTENT2");
        assertEquals(httpResponse.getHeader(DefaultInvalidationService.INVALIDATION_HEADER), "o=1;v=2;");
        assertEquals(cacheProvider.createCache(DefaultHttpCache.CACHE_NAME).getSize(), 1);
    }
