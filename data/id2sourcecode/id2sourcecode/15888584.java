    public static String getResponse(String url, String encoding) throws ClientProtocolException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.content-charset", encoding);
        BasicHttpContext localContext = new BasicHttpContext();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget, localContext);
        HttpHost target = (HttpHost) localContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        System.out.println("Final target: " + target);
        InputStream is = response.getEntity().getContent();
        String result = IOUtils.toString(is, encoding);
        return result;
    }
