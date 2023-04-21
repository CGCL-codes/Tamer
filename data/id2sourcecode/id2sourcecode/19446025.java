    protected HttpResponse executeGetWithResponse(String url) throws AuthenticationException, IOException {
        url = serviceUrl(url);
        HttpGet request = new HttpGet(url);
        setCredentials(request);
        LOG.info("GET request : {}", url);
        return client.execute(request);
    }
