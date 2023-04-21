    public HttpResponse executeHttpPostRequest(String uri, Map<String, Object> parameters) throws HttpTestClientException {
        HttpPost httpPost = new HttpPost(uri);
        if (parameters != null) {
            httpPost.setParams(createBasicParameters(parameters));
        }
        return executePostHttp(httpPost);
    }
