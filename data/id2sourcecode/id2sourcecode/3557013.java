    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
        return delegate.execute(target, request);
    }
