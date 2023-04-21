    public void executeGet(String link) throws ClientProtocolException, IOException, JSONException, SAXException {
        link = analyzeLink(link);
        String url = constructUrl(link);
        get = new HttpGet(url);
        setHeaders(get);
        currentUrl = get.getURI().toString();
        HttpResponse response = client.execute(get);
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
                mcce.printStackTrace();
            }
        }
        log.debug("executed get: " + currentUrl);
        metaRefresh();
        analyzeStatusCode();
    }
