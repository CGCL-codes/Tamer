    public HttpResponseMessage execute(HttpMessage request, Map<String, Object> parameters) throws IOException {
        final String method = request.method;
        final String url = request.url.toExternalForm();
        final InputStream body = request.getBody();
        final boolean isDelete = DELETE.equalsIgnoreCase(method);
        final boolean isPost = POST.equalsIgnoreCase(method);
        final boolean isPut = PUT.equalsIgnoreCase(method);
        byte[] excerpt = null;
        HttpRequestBase httpRequest;
        if (isPost || isPut) {
            HttpEntityEnclosingRequestBase entityEnclosingMethod = isPost ? new HttpPost(url) : new HttpPut(url);
            if (body != null) {
                ExcerptInputStream e = new ExcerptInputStream(body);
                excerpt = e.getExcerpt();
                String length = request.removeHeaders(HttpMessage.CONTENT_LENGTH);
                entityEnclosingMethod.setEntity(new InputStreamEntity(e, (length == null) ? -1 : Long.parseLong(length)));
            }
            httpRequest = entityEnclosingMethod;
        } else if (isDelete) {
            httpRequest = new HttpDelete(url);
        } else {
            httpRequest = new HttpGet(url);
        }
        for (Map.Entry<String, String> header : request.headers) {
            httpRequest.addHeader(header.getKey(), header.getValue());
        }
        HttpParams params = httpRequest.getParams();
        for (Map.Entry<String, Object> p : parameters.entrySet()) {
            String name = p.getKey();
            String value = p.getValue().toString();
            if (FOLLOW_REDIRECTS.equals(name)) {
                params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.parseBoolean(value));
            } else if (READ_TIMEOUT.equals(name)) {
                params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.parseInt(value));
            } else if (CONNECT_TIMEOUT.equals(name)) {
                params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Integer.parseInt(value));
            }
        }
        params.setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
        HttpClient client = clientPool.getHttpClient(new URL(httpRequest.getURI().toString()));
        HttpResponse httpResponse = client.execute(httpRequest);
        return new HttpMethodResponse(httpRequest, httpResponse, excerpt, request.getContentCharset());
    }
