    private void login() throws ClientProtocolException, IOException {
        HttpResponse response = client.execute(new HttpGet(((AuthSource) source).getLoginUrl()));
        response.getEntity().consumeContent();
        HttpPost post = new HttpPost(((AuthSource) source).getLoginUrl());
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Properties params = ((AuthSource) source).getLoginParams();
        for (String key : params.stringPropertyNames()) nvps.add(new BasicNameValuePair(key, params.getProperty(key)));
        post.setEntity(new UrlEncodedFormEntity(nvps));
        response = client.execute(post);
        response.getEntity().consumeContent();
    }
