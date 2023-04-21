    public static String getTinyLink(String url) throws ClientProtocolException, IOException, JSONException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(TINYLINK_URI + URLEncoder.encode(url));
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity());
        }
        return url;
    }
