    public String addShare(String oauth_token, String oauth_token_secret, String openid, String format, Map<String, String> shares) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        String url = "http://openapi.qzone.qq.com/share/add_share";
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("format", format));
        parameters.add(new BasicNameValuePair("images", shares.get("images")));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_CONSUMER_KEY, Config.APP_ID));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_NONCE, OAuth.getOauthNonce()));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.OAUTH_SIGNATURE_METHOD_VALUE));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_TIMESTAMP, OAuth.getOauthTimestamp()));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_TOKEN, oauth_token));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_VERSION, OAuth.OAUTH_VERSION_VALUE));
        parameters.add(new BasicNameValuePair(OAuth.OPENID, openid));
        parameters.add(new BasicNameValuePair("title", shares.get("title")));
        parameters.add(new BasicNameValuePair("url", shares.get("url")));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_SIGNATURE, OAuth.getOauthSignature("POST", url, parameters, oauth_token_secret)));
        HttpPost sharePost = new HttpPost(url);
        sharePost.setHeader("Referer", "http://openapi.qzone.qq.com");
        sharePost.setHeader("Host", "openapi.qzone.qq.com");
        sharePost.setHeader("Accept-Language", "zh-cn");
        sharePost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        sharePost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
        DefaultHttpClient httpclient = HttpClientUtils.getHttpClient();
        HttpResponse loginPostRes = httpclient.execute(sharePost);
        String shareHtml = HttpClientUtils.getHtml(loginPostRes, "UTF-8", false);
        return shareHtml;
    }
