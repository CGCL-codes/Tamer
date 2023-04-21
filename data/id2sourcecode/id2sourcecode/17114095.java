    public HttpResponse executeHttpGetRequest(String uri) throws HttpTestClientException {
        HttpGet httpGet = new HttpGet(uri);
        return executeHttp(httpGet);
    }
