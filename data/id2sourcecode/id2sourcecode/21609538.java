    public static String getApiVersion() throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient(params);
        HttpGet get = new HttpGet(API_VERSION);
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity());
        }
        throw new IOException("bad http response:" + response.getStatusLine().getReasonPhrase());
    }
