    protected HttpResponse executeDeleteWithResponse(String url) throws AuthenticationException, IOException {
        if (!url.startsWith("http")) {
            url = SERVICE_BASE_URL + url;
        }
        HttpDelete request = new HttpDelete(url);
        setCredentials(request);
        return client.execute(request);
    }
