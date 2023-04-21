    public static HttpResponse doGet(HttpGet get, HttpClient client) throws Exception {
        client.getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        HttpResponse resp = client.execute(get);
        return resp;
    }
