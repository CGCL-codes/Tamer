    @Test
    public void authTypeNoneNotCached() throws Exception {
        HttpRequest request = new HttpRequest(DEFAULT_URI).setAuthType(AuthType.NONE);
        fetcher.response = new HttpResponse("response");
        HttpResponse response = pipeline.execute(request);
        assertEquals(request, fetcher.request);
        assertEquals(fetcher.response, response);
        assertEquals(response, cache.data.get(DEFAULT_URI));
        assertEquals(1, cache.readCount);
        assertEquals(1, cache.writeCount);
        assertEquals(1, fetcher.fetchCount);
    }
