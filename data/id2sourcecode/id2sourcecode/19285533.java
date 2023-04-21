    private HttpResponse openConnection(final DefaultHttpClient httpClient, final String url) throws IOException {
        HttpGet httpget = new HttpGet(url);
        return httpClient.execute(httpget);
    }
