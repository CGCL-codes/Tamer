    public void testServeInvalidatedContentWithFetcherError() throws Exception {
        oauth.httpResponse = CACHEABLE;
        HttpResponse httpResponse = requestPipeline.execute(signedRequest);
        service.invalidateUserResources(ImmutableSet.of("OwnerX"), appxToken);
        oauth.httpResponse = HttpResponse.error();
        httpResponse = requestPipeline.execute(signedRequest);
        assertEquals(httpResponse, CACHEABLE);
    }
