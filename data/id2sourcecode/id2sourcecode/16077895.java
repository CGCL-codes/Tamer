    public HttpResponse execute(HttpHost host, HttpRequest req, HttpContext ctx) throws IOException, ClientProtocolException {
        host = getCanonicalHost(host);
        HttpClient client = clients.get(host);
        if (client == null) {
            ServiceWrapper wrapper = factory.getWrapperWithName(host.toHostString());
            client = new ServiceWrappedHttpClient(backend, wrapper);
            clients.put(host, client);
        }
        return client.execute(host, req, ctx);
    }
