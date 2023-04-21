    public GGToken getToken(String username, String password) throws IllegalStateException, GGException, Exception {
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("method", "gg.auth.getToken"));
        qparams.add(new BasicNameValuePair("key", this.key));
        qparams.add(new BasicNameValuePair("username", username));
        qparams.add(new BasicNameValuePair("password", password));
        String url = REST_URL + "?" + URLEncodedUtils.format(qparams, "UTF-8");
        URI uri = new URI(url);
        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = httpClient.execute(httpget);
        int status = response.getStatusLine().getStatusCode();
        errorCheck(response, status);
        InputStream content = response.getEntity().getContent();
        GGToken token = JAXB.unmarshal(content, GGToken.class);
        this.token = token.auth.token;
        this.user = token.auth.user.name;
        return token;
    }
