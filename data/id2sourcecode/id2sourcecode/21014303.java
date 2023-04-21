    public static List<Update> GetFriendsUpdates() throws ClientProtocolException, IOException, IllegalStateException, SAXException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("www.goodreads.com");
        builder.path("updates/friends.xml");
        HttpGet getUpdatesRequest = new HttpGet(builder.build().toString());
        if (get_IsAuthenticated()) {
            _Consumer.sign(getUpdatesRequest);
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(getUpdatesRequest);
        Response updatesResponse = ResponseParser.parse(response.getEntity().getContent());
        return updatesResponse.get_Updates();
    }
