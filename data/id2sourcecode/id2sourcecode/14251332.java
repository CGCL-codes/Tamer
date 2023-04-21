    static String getUrlHtmlContent(String url) throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return HTTPUtil.toString(entity, BBSYanxiBodyParseHelper.BBS_CHARSET);
        } catch (Exception e) {
            throw new NetworkException(e);
        }
    }
