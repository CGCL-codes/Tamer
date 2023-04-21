    private static InputStream getInputStream(HttpGet httpget) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        return entity.getContent();
    }
