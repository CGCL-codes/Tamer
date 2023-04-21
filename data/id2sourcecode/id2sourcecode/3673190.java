    public HttpEntity getHttpEntity(String url) throws ClientProtocolException, IOException {
        HttpGet request = new HttpGet(url);
        addDefaultHeaders(request);
        request.addHeader(HTTP_HEADER_REFERER, "http://us.playstation.com/playstation/psn/profile/friends?id=" + Math.random());
        HttpResponse response = httpClient.execute(request, context);
        return response.getEntity();
    }
