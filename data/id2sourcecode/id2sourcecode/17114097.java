    public HttpResponse executeHttpPostRequest(String uri) throws HttpTestClientException {
        HttpPost httpPost = new HttpPost(uri);
        return executePostHttp(httpPost);
    }
