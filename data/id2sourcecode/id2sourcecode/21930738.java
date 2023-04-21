    @Test
    public void authTypeNoneWasCachedButStale() throws Exception {
        HttpRequest request = new HttpRequest(DEFAULT_URI).setAuthType(AuthType.NONE);
        HttpResponse cached = new HttpResponseBuilder().setStrictNoCache().create();
        cache.data.put(DEFAULT_URI, cached);
        HttpResponse fetched = new HttpResponse("fetched");
        fetcher.response = fetched;
        HttpResponse response = pipeline.execute(request);
        assertEquals(fetched, response);
        assertEquals(request, fetcher.request);
        assertEquals(fetched, cache.data.get(DEFAULT_URI));
        assertEquals(1, cache.readCount);
        assertEquals(1, cache.writeCount);
        assertEquals(1, fetcher.fetchCount);
    }
