    public static InputStream post4(String uri, List<? extends NameValuePair> params) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(uri);
        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        try {
            HttpResponse response = new DefaultHttpClient().execute(post);
            return response.getEntity().getContent();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
