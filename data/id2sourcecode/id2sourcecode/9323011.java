    public String doGetBody(String encodedQueryStr) throws ClientProtocolException, IOException {
        System.out.println(super.getClass().getName() + "#doGetBody: \n encoded query: " + encodedQueryStr);
        String responseBody = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(encodedQueryStr);
        HttpResponse response = client.execute(get);
        responseBody = EntityUtils.toString(response.getEntity());
        return responseBody;
    }
