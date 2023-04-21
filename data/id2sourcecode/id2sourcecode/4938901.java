    public static HttpResponse execute(HttpHost host, HttpGet get) throws IOException {
        return sClient.execute(host, get);
    }
