    public static List<Event> GetNearbyEvents(Location location) throws Exception {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("www.goodreads.com");
        builder.path("event/index.xml");
        builder.appendQueryParameter("key", _ConsumerKey);
        builder.appendQueryParameter("lat", Double.toString(location.getLatitude()));
        builder.appendQueryParameter("lng", Double.toString(location.getLongitude()));
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(builder.build().toString());
        if (get_IsAuthenticated()) {
            _Consumer.sign(getRequest);
        }
        HttpResponse response = httpClient.execute(getRequest);
        Response responseData = ResponseParser.parse(response.getEntity().getContent());
        return responseData.get_Events();
    }
