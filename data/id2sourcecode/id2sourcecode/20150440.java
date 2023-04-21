    public String accessURL(String url) throws Throwable {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        String entuity = EntityUtils.toString(response.getEntity());
        handleHeaders(response.getAllHeaders());
        return entuity;
    }
