    public static void SendFriendRequest(String userId) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.goodreads.com/friend/add_as_friend.xml");
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("id", userId));
        post.setEntity(new UrlEncodedFormEntity(parameters));
        _Consumer.sign(post);
        HttpResponse response = httpClient.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            throw new Exception(response.getStatusLine().toString());
        }
    }
