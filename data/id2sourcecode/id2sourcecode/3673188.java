    public ArrayList<String> getFriendNames() throws ClientProtocolException, IOException, FriendListRetrievalException {
        HttpGet request = new HttpGet("http://us.playstation.com/playstation/psn/profile/friends?id=" + Math.random());
        addDefaultHeaders(request);
        request.addHeader(HTTP_HEADER_REFERER, "http://us.playstation.com/myfriends/index.htm");
        HttpResponse response = httpClient.execute(request, context);
        ArrayList<String> names = getFriendName(response.getEntity().getContent());
        response.getEntity().consumeContent();
        return names;
    }
