    public HtmlSpider(Pages config, Map<String, String> loginParameters) throws IOException {
        this.config = config;
        httpClient.getParams().setParameter("http.useragent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        MySocketFactory.patchHttpClient(httpClient);
        if (config.getAuthentication() != null) {
            if (config.getAuthentication().getFormPage() != null) {
                HtmlHelper.justVisitPage(new URL(config.getAuthentication().getFormPage()), httpClient);
            }
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            List<Pages.Authentication.Param> paramList = config.getAuthentication().getParam();
            for (Pages.Authentication.Param param : paramList) {
                params.add(new BasicNameValuePair(param.getName(), getValue(loginParameters.get(param.getName()), param.getValue())));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
            HttpPost post = new HttpPost(config.getAuthentication().getPage());
            post.setEntity(entity);
            HttpResponse response = httpClient.execute(post);
            response.getEntity().consumeContent();
        }
    }
