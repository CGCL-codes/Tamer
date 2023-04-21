    static String getUrlHtmlContent(String url) throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(url);
        get.addHeader("User-Agent", "Java/1.6.0_20");
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return HTTPUtil.toString(entity, BBSSjtuBodyParseHelper.BBS_CHARSET);
        } catch (Exception e) {
            throw new NetworkException(e);
        }
    }
