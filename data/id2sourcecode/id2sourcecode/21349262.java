    public String getRedirectedUrl(final String url) throws IOException {
        final HttpGet httpget = new HttpGet(url);
        final HttpContext context = new BasicHttpContext();
        final HttpResponse response = httpClient.execute(httpget, context);
        final HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        final HttpHost currentHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        final HttpEntity entity = response.getEntity();
        if (entity != null) {
            entity.consumeContent();
        }
        return currentHost.toURI() + currentReq.getURI();
    }
