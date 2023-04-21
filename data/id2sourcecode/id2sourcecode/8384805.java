    private void expectGetAndReturnBody(AuthType authType, String response) throws Exception {
        HttpRequest request = new HttpRequest(REQUEST_URL).setAuthType(authType);
        expect(pipeline.execute(request)).andReturn(new HttpResponse(response));
    }
