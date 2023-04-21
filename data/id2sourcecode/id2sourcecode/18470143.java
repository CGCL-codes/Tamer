    public void testGeoCodeAddress() throws Exception {
        log.info("Starting PlaceFinder Address GeoCoding........");
        String appId = "91XdMe7k";
        String url = "http://where.yahooapis.com/geocode?location=37.787082+-122.400929&gflags=R&appid=" + appId;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        String json = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            json = EntityUtils.toString(entity);
        }
        System.out.println(json);
    }
