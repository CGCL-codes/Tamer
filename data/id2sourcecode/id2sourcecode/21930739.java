    @Test
    public void authTypeNoneIgnoreCache() throws Exception {
        HttpRequest request = new HttpRequest(DEFAULT_URI).setAuthType(AuthType.NONE).setIgnoreCache(true);
        HttpResponse fetched = new HttpResponse("fetched");
        fetcher.response = fetched;
        HttpResponse response = pipeline.execute(request);
        assertEquals(fetched, response);
        assertEquals(request, fetcher.request);
        assertEquals(0, cache.readCount);
        assertEquals(0, cache.writeCount);
        assertEquals(1, fetcher.fetchCount);
    }
