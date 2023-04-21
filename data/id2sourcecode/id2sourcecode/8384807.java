    private void expectPostAndReturnBody(AuthType authType, String postData, String response) throws Exception {
        HttpRequest req = new HttpRequest(REQUEST_URL).setMethod("POST").setPostBody(REQUEST_BODY.getBytes("UTF-8")).setAuthType(authType).addHeader("Content-Type", "application/x-www-form-urlencoded");
        expect(pipeline.execute(req)).andReturn(new HttpResponse(response));
        expect(request.getParameter(MakeRequestHandler.METHOD_PARAM)).andReturn("POST");
        expect(request.getParameter(MakeRequestHandler.POST_DATA_PARAM)).andReturn(REQUEST_BODY);
    }
