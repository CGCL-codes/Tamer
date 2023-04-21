    protected int getHeadForWebPageUsingHTTPClient(String urlString) throws ClientProtocolException, IOException {
        log("Retrieving url HEAD: " + urlString);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        if (this.archiveAccessSpecification.getUserID() != null) {
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(this.archiveAccessSpecification.getUserID(), this.archiveAccessSpecification.getUserPassword()));
        }
        HttpHead httpget = new HttpHead(urlString);
        log("about to do request: " + httpget.getRequestLine());
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        log("-------------- Request results --------------");
        log("Status line: " + response.getStatusLine());
        if (entity != null) {
            log("Response content length: " + entity.getContentLength());
        }
        if (entity != null) {
            EntityUtils.consume(entity);
        }
        int statusCode = response.getStatusLine().getStatusCode();
        log("Status code: " + statusCode);
        log(response.getStatusLine().getReasonPhrase());
        return statusCode;
    }
