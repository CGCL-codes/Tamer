    public void onSearch(Event evt) {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        BasicHttpContext localcontext = new BasicHttpContext();
        String searchString = searchText.getValue().trim().replaceAll("\\s+", "+");
        HttpGet httpget = new HttpGet("/geoserver/rest/gazetteer-search/result.json?q=" + searchString);
        try {
            HttpResponse response = httpclient.execute(targetHost, httpget, localcontext);
            HttpEntity entity = response.getEntity();
            String responseText = "";
            if (entity != null) {
                responseText = new String(EntityUtils.toByteArray(entity));
            } else {
                responseText = "Fail";
            }
            resultLabel.setValue(responseText);
        } catch (Exception e) {
        }
    }
