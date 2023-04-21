    public static void PostComment(String resourceId, String resourceType, String comment) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.goodreads.com/comment.xml");
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("comment[body]", comment));
        parameters.add(new BasicNameValuePair("id", resourceId));
        parameters.add(new BasicNameValuePair("type", resourceType));
        post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
        _Consumer.sign(post);
        HttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new Exception(response.getStatusLine().toString());
        }
    }
