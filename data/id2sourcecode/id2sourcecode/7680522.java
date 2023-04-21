    private static String postRequest(String params, String contentType) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        post.setHeader(HTTP.CONTENT_TYPE, contentType);
        post.setEntity(new StringEntity(params));
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity());
        }
        throw new IOException(response.getStatusLine().getReasonPhrase());
    }
