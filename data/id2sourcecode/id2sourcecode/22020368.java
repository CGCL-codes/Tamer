    public HttpResponseMessage sendGetRequest(Map<String, String> listOfMethods) throws URISyntaxException, ClientProtocolException, IOException {
        HttpResponseMessage rslt = null;
        String sig = Utility.convetToSignature(listOfMethods, apiSecret);
        URI uri = Utility.createURI(listOfMethods, PROTOCOL, SERVER_ADDRESS, RESOURCE_URL, sig, null);
        HttpGet get = new HttpGet(uri);
        HttpResponse resp = httpclient.execute(get);
        StatusLine status = resp.getStatusLine();
        HttpEntity respEntity = resp.getEntity();
        InputStream content = respEntity.getContent();
        rslt = new HttpResponseMessage("GET", uri.toURL(), status.getStatusCode(), content);
        return rslt;
    }
