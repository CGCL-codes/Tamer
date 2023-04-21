    public static String getGetResult(URI uri) throws Exception {
        HttpGet httpget = new HttpGet(uri);
        HttpClient httpclient = getSimpleHttpClient();
        HttpResponse httpResponse = httpclient.execute(httpget);
        return getResponseContent(httpResponse);
    }
