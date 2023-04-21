    public static List<UserShelf> GetShelvesForUser(String userId) throws Exception {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("www.goodreads.com");
        builder.path("shelf/list");
        builder.appendQueryParameter("format", "xml");
        builder.appendQueryParameter("user_id", userId);
        builder.appendQueryParameter("key", _ConsumerKey);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getShelvesRequest = new HttpGet(builder.build().toString());
        if (get_IsAuthenticated()) {
            _Consumer.sign(getShelvesRequest);
        }
        HttpResponse shelvesResponse = httpClient.execute(getShelvesRequest);
        Response shelvesResponseData = ResponseParser.parse(shelvesResponse.getEntity().getContent());
        return shelvesResponseData.get_Shelves().get_UserShelves();
    }
