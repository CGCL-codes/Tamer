    @Test
    public void authTypeOAuthNotCached() throws Exception {
        HttpRequest request = new HttpRequest(DEFAULT_URI).setAuthType(AuthType.OAUTH);
        oauth.httpResponse = new HttpResponse("oauth result");
        HttpResponse response = pipeline.execute(request);
        assertEquals(oauth.httpResponse, response);
        assertEquals(request, oauth.httpRequest);
        assertEquals(response, cache.data.get(DEFAULT_URI));
        assertEquals(1, oauth.fetchCount);
        assertEquals(0, fetcher.fetchCount);
        assertEquals(1, cache.readCount);
        assertEquals(1, cache.writeCount);
    }
