    public static void PostStatusUpdate(String comment) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.goodreads.com/user_status.xml");
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("user_status[body]", comment));
        post.setEntity(new UrlEncodedFormEntity(parameters));
        _Consumer.sign(post);
        HttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new Exception(response.getStatusLine().toString());
        }
    }
