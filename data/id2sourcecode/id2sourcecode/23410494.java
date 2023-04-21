    @Override
    public HttpResponse execute(int retries, RequestSetup setup) throws IOException {
        Request req = new Request();
        setup.setup(req);
        HTTPRequest request = req.getRequest();
        return new Response(retries, request);
    }
