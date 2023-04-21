    protected HttpResponse executePost(String url, HttpEntity entity, Header... headers) throws AuthenticationException, UnsupportedEncodingException, IOException, ClientProtocolException {
        url = serviceUrl(url);
        HttpPost request = new HttpPost(url);
        if (headers != null) {
            for (Header header : headers) {
                request.addHeader(header);
            }
        }
        setCredentials(request);
        request.setEntity(entity);
        LOG.info("POST request : {}", url);
        return client.execute(request);
    }
