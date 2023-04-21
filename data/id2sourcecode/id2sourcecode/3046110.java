    public GGUserInfo getUserInfo(String userId) throws IllegalStateException, GGException, Exception {
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("method", "gg.people.getInfo"));
        qparams.add(new BasicNameValuePair("key", this.key));
        qparams.add(new BasicNameValuePair("user_id", userId));
        String url = REST_URL + "?" + URLEncodedUtils.format(qparams, "UTF-8");
        URI uri = new URI(url);
        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = httpClient.execute(httpget);
        int status = response.getStatusLine().getStatusCode();
        errorCheck(response, status);
        InputStream content = response.getEntity().getContent();
        GGUserInfo userInfo = JAXB.unmarshal(content, GGUserInfo.class);
        return userInfo;
    }
