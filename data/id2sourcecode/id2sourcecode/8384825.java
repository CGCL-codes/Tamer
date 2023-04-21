    public void testBadHttpResponseIsPropagated() throws Exception {
        HttpRequest internalRequest = new HttpRequest(REQUEST_URL);
        expect(pipeline.execute(internalRequest)).andReturn(HttpResponse.error());
        replay();
        handler.fetch(request, recorder);
        JSONObject results = extractJsonFromResponse();
        assertEquals(HttpResponse.SC_INTERNAL_SERVER_ERROR, results.getInt("rc"));
        assertTrue(rewriter.responseWasRewritten());
    }
