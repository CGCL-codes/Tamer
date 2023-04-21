    public synchronized String doGet(String url, String... params) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpProtocolParams.setUserAgent(httpclient.getParams(), EnvironmentConstants.HTTPCLIENT_USERAGENT);
        String charset = EncodingConstants.UTF8_ENCODING;
        if (null != params && params.length > 0) {
            charset = params[0];
        }
        HttpGet httpget = new HttpGet();
        String content = SystemConstants.EMPTY_STRING;
        httpget.setURI(new java.net.URI(url));
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            content = EntityUtils.toString(entity, charset);
            httpget.abort();
            httpclient.getConnectionManager().shutdown();
        }
        return content;
    }
