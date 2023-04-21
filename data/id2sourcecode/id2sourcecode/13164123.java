    public int doStartTag() throws JspException {
        try {
            AssertUtility.notNullAndNotSpace(url);
            AssertUtility.notNullAndNotSpace(feedId);
            HttpParams params = new BasicHttpParams();
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
            DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost);
            Document document = DocumentHelper.parseText(EntityUtils.toString(response.getEntity(), "UTF-8"));
            webutil.setRequestAttribute(feedId, document);
        } catch (Exception e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
