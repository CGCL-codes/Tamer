    protected static HttpResponse doGet(HttpGet get, HttpClient client, HttpContext context) throws Exception {
        client.getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        HttpResponse resp = client.execute(get, context);
        return resp;
    }
