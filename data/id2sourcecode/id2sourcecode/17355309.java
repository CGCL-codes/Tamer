    @SuppressWarnings("unchecked")
    public ClientResponse execute(ClientRequest request) throws Exception {
        String uri = request.getUri();
        final HttpRequestBase httpMethod = createHttpMethod(uri, request.getHttpMethod());
        loadHttpMethod(request, httpMethod);
        final HttpResponse res = httpClient.execute(httpMethod, httpContext);
        BaseClientResponse response = new BaseClientResponse(new BaseClientResponseStreamFactory() {

            InputStream stream;

            public InputStream getInputStream() throws IOException {
                if (stream == null) {
                    HttpEntity entity = res.getEntity();
                    if (entity == null) return null;
                    stream = new SelfExpandingBufferredInputStream(entity.getContent());
                }
                return stream;
            }

            public void performReleaseConnection() {
                try {
                    if (stream != null) {
                        stream.close();
                    } else {
                        InputStream is = getInputStream();
                        if (is != null) {
                            is.close();
                        }
                    }
                } catch (Exception ignore) {
                }
            }
        }, this);
        response.setStatus(res.getStatusLine().getStatusCode());
        response.setHeaders(extractHeaders(res));
        response.setProviderFactory(request.getProviderFactory());
        return response;
    }
