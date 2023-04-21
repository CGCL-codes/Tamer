    public static InputStream get(String uri) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(uri);
        HttpResponse response = httpClient.execute(get);
        return response.getEntity().getContent();
    }
