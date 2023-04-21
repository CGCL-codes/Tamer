    public HttpResponse execute(AbstractHttpRequest request) throws IOException, RequestCancelledException, TimeoutException {
        return request.makeRequest();
    }
