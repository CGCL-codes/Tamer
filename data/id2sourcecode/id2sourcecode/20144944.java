    public static void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpHost proxy = new HttpHost("127.0.0.1", 8888);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        HttpGet httpget = new HttpGet("http://cn.bing.com");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            System.out.println(EntityUtils.toString(entity));
        }
    }
