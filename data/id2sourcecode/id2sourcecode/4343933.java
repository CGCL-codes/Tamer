    public void testPlacesRequest() throws Exception {
        log.info("Starting Google Place Search........");
        String apiKey = "AIzaSyAntv38LTmUTBSlSLHzX-XbfNFcl4F5zrA";
        String url = "https://maps.googleapis.com/maps/api/place/search/xml?location=-33.8670522,151.1957362" + "&radius=500&types=food&name=harbour&sensor=false&key=" + apiKey;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        String result = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
        }
        System.out.println(result);
    }
