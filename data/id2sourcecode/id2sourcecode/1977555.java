    private HttpResponse executeMethod(HttpUriRequest httpUriRequest) throws IOException {
        if (log.isDebugEnabled()) {
            for (Header header : httpUriRequest.getAllHeaders()) {
                log.debug(String.format("LIGHTHTTP. Request header: [%s: %s]", header.getName(), header.getValue()));
            }
        }
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        return httpClient.execute(httpUriRequest, localContext);
    }
