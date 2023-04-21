    public HttpRequestResult execute(HttpUriRequest request) throws IOException {
        return httpClient.execute(request, new HttpResponseHandler());
    }
