    public void executePost(String link, boolean isMultiPartData) throws ClientProtocolException, IOException, JSONException, SAXException {
        link = analyzeLink(link);
        String url = constructUrl(link);
        post = new HttpPost(url);
        setHeaders(post);
        JSONArray cf_names = custom_formdata.names();
        if (cf_names != null) {
            if (isMultiPartData) {
                MultipartEntity reqEntity = new MultipartEntity();
                for (int i = 0; i < cf_names.length(); i++) {
                    String name = cf_names.getString(i);
                    ContentBody cb = (ContentBody) custom_formdata.get(name);
                    reqEntity.addPart(name, cb);
                }
                post.setEntity(reqEntity);
            } else {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (int i = 0; i < cf_names.length(); i++) {
                    String name = cf_names.getString(i);
                    String value = custom_formdata.getString(name);
                    nvps.add(new BasicNameValuePair(name, value));
                }
                post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            }
            custom_formdata = new JSONObject();
        }
        currentUrl = post.getURI().toString();
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        currentStatusCode = response.getStatusLine().getStatusCode();
        currentTextStatus = response.getStatusLine().toString();
        currentHeaders = new HashMap<String, String>();
        for (Header h : response.getAllHeaders()) {
            currentHeaders.put(h.getName(), h.getValue());
        }
        currentCookies = new StringBuilder();
        for (Cookie c : client.getCookieStore().getCookies()) {
            currentCookies.append(c + "\n");
        }
        if (entity != null) {
            try {
                currentHtml = EntityUtils.toString(entity);
                entity.consumeContent();
            } catch (MalformedChunkCodingException mcce) {
            }
        }
        log.debug("executed post: " + currentUrl);
        metaRefresh();
        analyzeStatusCode();
    }
