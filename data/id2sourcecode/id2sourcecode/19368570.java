    public static void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getCredentialsProvider().setCredentials(new AuthScope("localhost", 8080), new UsernamePasswordCredentials("username", "password"));
        HttpHost targetHost = new HttpHost("www.verisign.com", 443, "https");
        HttpHost proxy = new HttpHost("localhost", 8080);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        HttpGet httpget = new HttpGet("/");
        System.out.println("executing request: " + httpget.getRequestLine());
        System.out.println("via proxy: " + proxy);
        System.out.println("to target: " + targetHost);
        HttpResponse response = httpclient.execute(targetHost, httpget);
        HttpEntity entity = response.getEntity();
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
        }
        if (entity != null) {
            entity.consumeContent();
        }
    }
