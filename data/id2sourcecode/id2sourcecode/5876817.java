    private HttpResponse executeOnce(RequestSetup setup) throws IOException {
        DefaultRequestDefinition req = new DefaultRequestDefinition();
        setup.setup(req);
        HttpResponse response = req.execute();
        response.getResponseCode();
        return response;
    }
