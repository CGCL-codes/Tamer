    public void testSignedGetRequest() throws Exception {
        expect(request.getAttribute(AuthInfo.Attribute.SECURITY_TOKEN.getId())).andReturn(DUMMY_TOKEN).atLeastOnce();
        expect(request.getParameter(MakeRequestHandler.AUTHZ_PARAM)).andReturn(AuthType.SIGNED.toString()).atLeastOnce();
        HttpRequest expected = new HttpRequest(REQUEST_URL).setAuthType(AuthType.SIGNED);
        expect(pipeline.execute(expected)).andReturn(new HttpResponse(RESPONSE_BODY));
        expectParameters(request, MakeRequestHandler.AUTHZ_PARAM);
        replay();
        handler.fetch(request, recorder);
        JSONObject results = extractJsonFromResponse();
        assertEquals(RESPONSE_BODY, results.get("body"));
        assertTrue(rewriter.responseWasRewritten());
    }
