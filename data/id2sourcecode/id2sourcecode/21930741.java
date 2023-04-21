    @Test
    public void authTypeOAuthWasCached() throws Exception {
        HttpRequest request = new HttpRequest(DEFAULT_URI).setAuthType(AuthType.OAUTH);
        HttpResponse cached = new HttpResponse("cached");
        cache.data.put(DEFAULT_URI, cached);
        HttpResponse response = pipeline.execute(request);
        assertEquals(cached, response);
        assertEquals(0, oauth.fetchCount);
        assertEquals(0, fetcher.fetchCount);
        assertEquals(1, cache.readCount);
        assertEquals(0, cache.writeCount);
    }
