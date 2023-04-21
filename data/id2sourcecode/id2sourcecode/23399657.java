    public void testFetchContentSignedViewer() throws Exception {
        oauth.httpResponse = CACHEABLE;
        signedRequest.getOAuthArguments().setSignOwner(false);
        HttpResponse httpResponse = requestPipeline.execute(signedRequest);
        assertEquals(httpResponse, CACHEABLE);
        assertEquals(cacheProvider.createCache(DefaultHttpCache.CACHE_NAME).getSize(), 1);
        service.invalidateUserResources(ImmutableSet.of("OwnerX"), appxToken);
        oauth.httpResponse = new HttpResponseBuilder(CACHEABLE).setResponseString("NEWCONTENT1").create();
        httpResponse = requestPipeline.execute(signedRequest);
        assertEquals(httpResponse, CACHEABLE);
        service.invalidateUserResources(ImmutableSet.of("ViewerX"), appxToken);
        oauth.httpResponse = new HttpResponseBuilder(CACHEABLE).setResponseString("NEWCONTENT2").create();
        httpResponse = requestPipeline.execute(signedRequest);
        assertEquals(httpResponse.getResponseAsString(), "NEWCONTENT2");
        assertEquals(httpResponse.getHeader(DefaultInvalidationService.INVALIDATION_HEADER), "v=2;");
        assertEquals(cacheProvider.createCache(DefaultHttpCache.CACHE_NAME).getSize(), 1);
    }
