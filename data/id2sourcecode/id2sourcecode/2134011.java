    protected HttpResponse getResponse(HttpClient client, String url, String referer) throws ContactListImporterException, URISyntaxException, InterruptedException, HttpException, IOException {
        client.getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        HttpGet get = new HttpGet(url);
        setHeaders(get, referer);
        HttpResponse resp = client.execute(get, client.getDefaultContext());
        return resp;
    }
