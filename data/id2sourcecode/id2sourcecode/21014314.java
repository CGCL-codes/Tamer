    public static void PostStatusUpdate(String book, String page, String comment) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.goodreads.com/user_status.xml");
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("user_status[book_id]", book));
        parameters.add(new BasicNameValuePair("user_status[page]", page));
        parameters.add(new BasicNameValuePair("user_status[body]", comment));
        post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
        _Consumer.sign(post);
        HttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new Exception(response.getStatusLine().toString());
        }
    }
