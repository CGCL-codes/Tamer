    public static HttpResult getContent(URL url, HttpClient httpclient) throws IOException {
        HttpGet httpget = new HttpGet(url.toExternalForm());
        HttpResponse response = httpclient.execute(httpget);
        return new HttpResult(response.getEntity().getContent());
    }
