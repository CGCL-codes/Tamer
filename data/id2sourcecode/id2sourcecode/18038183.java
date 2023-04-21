    protected String httpToStringStupid(String url, String encoding) throws IllegalStateException, IOException, HttpException, InterruptedException, URISyntaxException {
        DefaultHttpClient httpclient = new CrawlerHttpClient(getPreferenceService());
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY);
        getLogger().debug("url fetch: " + url);
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String pageDump = IOUtils.toString(entity.getContent(), encoding);
        return pageDump;
    }
