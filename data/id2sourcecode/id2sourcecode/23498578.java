    public InputStream getInputStream(FileTransaction txt) throws InputOutputException {
        Assert.isNull(httpClient, getClass().toString() + " is not support multithreading");
        httpClient = new DefaultHttpClient();
        HttpRequestBase httpRequest;
        if (params != null) {
            httpRequest = params.createMethod(getSystemId());
            String contentType = params.createContentTypeString();
            if (contentType != null) {
                httpRequest.addHeader("Content-Type", contentType);
            }
            if (params.isAuthRequired()) {
                httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, params.createCredentials());
            }
        } else {
            httpRequest = new HttpGet(getSystemId());
        }
        try {
            if (params != null && params.isIgnoreSslChecks() && HTTPS.equals(httpRequest.getURI().getScheme())) {
                doNotValidateSslSertificate(httpRequest);
            }
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return httpResponse.getEntity().getContent();
            }
            InputOutputException e = new InputOutputException("Error on fetch " + getSystemId(), httpResponse.getStatusLine().getReasonPhrase());
            e.setAttr("status line", httpResponse.getStatusLine().toString());
            e.setAttr("statusCode", statusCode);
            throw e;
        } catch (Exception e) {
            throw new InputOutputException(e, getSystemId());
        }
    }
