    public HttpResponse execute(HttpHost host, HttpRequest req, HttpContext ctx) throws IOException, ClientProtocolException {
        return client.execute(host, req, ctx);
    }
