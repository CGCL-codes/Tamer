    public void authenticate(DefaultHttpClient httpclient, Log log) throws IOException {
        HttpPost httpPost = new HttpPost(formUrl);
        UsernamePasswordCredentials credentials = toUsernamePasswordCredentials();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new Parameter(loginParameterName, credentials.getUserName()).toNameValuePair());
        nvps.add(new Parameter(passwordParameterName, credentials.getPassword()).toNameValuePair());
        for (Parameter parameter : parameters) {
            nvps.add(parameter.toNameValuePair());
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        HttpResponse response = httpclient.execute(httpPost);
        EntityUtils.consume(response.getEntity());
        log.info("form authentication submitted to " + formUrl);
    }
