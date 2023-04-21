    private static byte[] getBytesForWebPageUsingHTTPClient(String urlString) throws Exception {
        println("Starting test");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials("test", "test2"));
        HttpGet httpget = new HttpGet(urlString);
        System.out.println("executing request" + httpget.getRequestLine());
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
        }
        println("contents");
        byte[] bytes = null;
        if (entity != null) {
            bytes = getBytesFromInputStream(entity.getContent());
            entity.consumeContent();
        }
        return bytes;
    }
