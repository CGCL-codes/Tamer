    protected HttpResponse handleRedirectIfNeeded(org.apache.http.HttpResponse responseToRedirect) throws IOException {
        Header locationHeader = responseToRedirect.getFirstHeader("Location");
        if (!handleRedirects || redirectsCount > MAX_REDIRECTS || locationHeader == null) {
            return new HttpResponse(responseToRedirect);
        } else {
            String location = locationHeader.getValue();
            String host = ((HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST)).toURI();
            String redirectedUrl = getRedirectLocation(host, location);
            responseToRedirect.getEntity().consumeContent();
            HttpGet get = new HttpGet(redirectedUrl);
            org.apache.http.HttpResponse response = httpClient.execute(get, context);
            redirectsCount++;
            return handleRedirectIfNeeded(response);
        }
    }
