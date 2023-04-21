    @Test
    public void authTypeNoneWasCached() throws Exception {
        HttpRequest request = new HttpRequest(DEFAULT_URI).setAuthType(AuthType.NONE);
        HttpResponse cached = new HttpResponse("cached");
        cache.data.put(DEFAULT_URI, cached);
        HttpResponse response = pipeline.execute(request);
        assertEquals(cached, response);
        assertEquals(1, cache.readCount);
        assertEquals(0, cache.writeCount);
        assertEquals(0, fetcher.fetchCount);
    }
