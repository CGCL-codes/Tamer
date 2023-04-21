    public static User GetAuthorizedUser() throws Exception {
        HttpGet getRequest = new HttpGet("http://www.goodreads.com/api/auth_user");
        _Consumer.sign(getRequest);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(getRequest);
        Response responseData = ResponseParser.parse(response.getEntity().getContent());
        return responseData.get_User();
    }
