    public void getBinaryData(OutputStream os, String link) throws JSONException, ClientProtocolException, IOException {
        link = analyzeLink(link);
        String url = constructUrl(link);
        get = new HttpGet(url);
        setHeaders(get);
        currentUrl = this.get.getURI().toString();
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
                InputStream in = entity.getContent();
                byte[] buffer = new byte[1024];
                int count = -1;
                while ((count = in.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
                os.close();
                entity.consumeContent();
            } catch (MalformedChunkCodingException mcce) {
                mcce.printStackTrace();
            }
        }
        log.debug("image retrieved: " + currentUrl);
    }
