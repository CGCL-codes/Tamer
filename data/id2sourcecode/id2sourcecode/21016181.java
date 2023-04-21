    private static InputStream getStreamFromURL(String tapURL, String expression) throws IllegalStateException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(tapURL);
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("REQUEST", "doQuery"));
        postParameters.add(new BasicNameValuePair("VERSION", "1.0"));
        postParameters.add(new BasicNameValuePair("LANG", "ADQL"));
        postParameters.add(new BasicNameValuePair("QUERY", expression));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
        post.setEntity(formEntity);
        HttpResponse response = client.execute(post);
        return response.getEntity().getContent();
    }
