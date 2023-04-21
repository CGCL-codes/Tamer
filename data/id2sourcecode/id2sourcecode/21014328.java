    public static List<Event> GetEvents() throws Exception {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("www.goodreads.com");
        builder.path("event/index.xml");
        builder.appendQueryParameter("key", _ConsumerKey);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(builder.build().toString());
        if (get_IsAuthenticated()) {
            _Consumer.sign(getRequest);
        }
        HttpResponse response = httpClient.execute(getRequest);
        Response responseData = ResponseParser.parse(response.getEntity().getContent());
        return responseData.get_Events();
    }
