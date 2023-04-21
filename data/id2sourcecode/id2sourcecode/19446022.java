    protected HttpResponse executePut(String url, HttpEntity entity, Header... headers) throws AuthenticationException, UnsupportedEncodingException, IOException, ClientProtocolException {
        url = serviceUrl(url);
        HttpPut request = new HttpPut(url);
        for (Header header : headers) {
            request.addHeader(header);
        }
        setCredentials(request);
        request.setEntity(entity);
        LOG.info("PUT request : {}", url);
        return client.execute(request);
    }
